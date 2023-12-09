package com.totoro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.totoro.entity.dto.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author: totoro
 * @createDate: 2023 12 09 23 10
 * @description:
 **/
public interface AccountService extends IService<Account>, UserDetailsService {

    Account findAccountByNameOrEmail(String text);
}
