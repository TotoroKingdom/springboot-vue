package com.totoro.controller;

import com.totoro.entity.RestBean;
import com.totoro.entity.vo.request.ConfirmResetVO;
import com.totoro.entity.vo.request.EmailRegisterVO;
import com.totoro.entity.vo.request.EmailResetVO;
import com.totoro.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

        return this.messageHandle(() -> message);

    }

    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo
                                   ){
        return this.messageHandle(vo, accountService::registerEmailAccount);

    }

    @PostMapping("/reset-confirm")
    public RestBean<Void> resetConfirm(@RequestBody @Valid ConfirmResetVO vo){
        return this.messageHandle(vo, accountService::resetConfirm);
    }

    @PostMapping("/reset-password")
    public RestBean<Void> resetPassword(@RequestBody @Valid EmailResetVO vo){
        return this.messageHandle(vo, accountService::resetEmailAccountPassword);
    }

    private <T> RestBean<Void> messageHandle(T vo, Function<T, String> function){
        return messageHandle(() -> function.apply(vo));
    }

    private RestBean<Void> messageHandle(Supplier<String> action){
        String message = action.get();
        return message == null? RestBean.success(): RestBean.failure(400, message);

    }

}
