package com.totoro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.totoro.entity.dto.Account;
import com.totoro.entity.vo.request.ConfirmResetVO;
import com.totoro.entity.vo.request.EmailRegisterVO;
import com.totoro.entity.vo.request.EmailResetVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author: totoro
 * @createDate: 2023 12 09 23 10
 * @description:
 **/
public interface AccountService extends IService<Account>, UserDetailsService {

    Account findAccountByNameOrEmail(String text);

    /**
     * 邮件注册
     * @param type
     * @param email
     * @param ip
     * @return
     */
    String registerEmailVerifyCode(String type, String email, String ip);

    String registerEmailAccount(EmailRegisterVO vo);

    String resetConfirm(ConfirmResetVO vo);

    String resetEmailAccountPassword(EmailResetVO vo);
}
