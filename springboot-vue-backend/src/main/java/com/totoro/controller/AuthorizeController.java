package com.totoro.controller;

import com.totoro.entity.RestBean;
import com.totoro.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: totoro
 * @createDate: 2023 12 11 17 33
 * @description:
 **/
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Resource
    private AccountService accountService;

    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam String email
                                        , @RequestParam String type
                                        , HttpServletRequest request
                                        ){
        String message = accountService.registerEmailVerifyCode(type, email, request.getRemoteAddr());

        return message == null? RestBean.success(): RestBean.failure(400, message);


    }

}
