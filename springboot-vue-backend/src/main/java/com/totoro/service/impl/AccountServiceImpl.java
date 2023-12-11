package com.totoro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.totoro.entity.dto.Account;
import com.totoro.mapper.AccountMapper;
import com.totoro.service.AccountService;
import com.totoro.utils.Const;
import com.totoro.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author: totoro
 * @createDate: 2023 12 09 23 10
 * @description:
 **/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService  {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FlowUtils flowUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = this.findAccountByNameOrEmail(username);

        if (account == null){
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();

    }

    @Override
    public Account findAccountByNameOrEmail(String text){
        Account one = this.query().eq("username", text)
                .or()
                .eq("email", text)
                .one();
        return one;
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()){
            if (!this.verifyLimit(ip)){
                return "请求频繁";
            }

            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data = Map.of("type",type,"email",email,"code",code);

            amqpTemplate.convertAndSend("mail",data);

            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA+email,String.valueOf(code),3, TimeUnit.MINUTES);

            return null;

        }

    }

    private boolean verifyLimit(String ip){
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }

}
