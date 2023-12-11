package com.totoro.controller;

import com.totoro.entity.RestBean;
import com.totoro.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: totoro
 * @createDate: 2023 12 11 17 33
 * @description:
 **/
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Resource
    private AccountService accountService;

    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email
                                        , @RequestParam @Pattern(regexp = "(register|reset)") String type
                                        , HttpServletRequest request
                                        ){
        String message = accountService.registerEmailVerifyCode(type, email, request.getRemoteAddr());

        return message == null? RestBean.success(): RestBean.failure(400, message);


    }

}
