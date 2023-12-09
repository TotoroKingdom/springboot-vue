package com.totoro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.totoro.entity.dto.Account;
import com.totoro.mapper.AccountMapper;
import com.totoro.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author: totoro
 * @createDate: 2023 12 09 23 10
 * @description:
 **/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService  {

    @Resource
    private AccountMapper accountMapper;

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


}
