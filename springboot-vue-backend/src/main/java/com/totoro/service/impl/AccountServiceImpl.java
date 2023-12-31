package com.totoro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.totoro.entity.dto.Account;
import com.totoro.entity.vo.request.ConfirmResetVO;
import com.totoro.entity.vo.request.EmailRegisterVO;
import com.totoro.entity.vo.request.EmailResetVO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.Date;
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

    @Resource
    PasswordEncoder encoder;

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

    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {

        String email = vo.getEmail();
        String username = vo.getUsername();
        String key = Const.VERIFY_EMAIL_DATA + email;
        String code = stringRedisTemplate.opsForValue().get(key);
        if (code == null){
            return "请先获取验证码";
        }
        if (!code.equals(vo.getCode())){
            return "验证码错误，请重新输入";
        }
        if (existsAccountByEmail(email)){
            return "此电子邮件已被其他用户注册";
        }
        if (existsAccountByUsername(username)){
            return "此用户名已被其他用户注册，更新一个新的用户名";
        }
        String password = encoder.encode(vo.getPassword());

        Account account = new Account(null, username, password, email, "user", new Date());
        boolean save = this.save(account);
        if (save){
            stringRedisTemplate.delete(key);
            return null;
        }else {
            return "内部错误，请联系管理员";
        }

    }

    @Override
    public String resetConfirm(ConfirmResetVO vo) {
        String email = vo.getEmail();
        String code = stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + email);

        if (code == null){
            return "请先获取验证码";
        }

        if (!code.equals(vo.getCode())){
            return "验证码错误，请重新输入";
        }
        return null;
    }

    @Override
    public String resetEmailAccountPassword(EmailResetVO vo) {
        String email = vo.getEmail();
        String verify = this.resetConfirm(new ConfirmResetVO(email, vo.getCode()));

        if (verify != null){
            return verify;
        }
        String password = encoder.encode(vo.getPassword());
        boolean update = this.update().eq("email", email).set("password", password).update();
        if (update){
            stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA + email);
        }

        return null;
    }

    private boolean existsAccountByEmail(String email){
        QueryWrapper<Account> wrapper = Wrappers.<Account>query().eq("email", email);
        return this.baseMapper.exists(wrapper);
    }

    private boolean existsAccountByUsername(String username){
        QueryWrapper<Account> wrapper = Wrappers.<Account>query().eq("username", username);
        return this.baseMapper.exists(wrapper);
    }



    private boolean verifyLimit(String ip){
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key, 60);
    }

}
