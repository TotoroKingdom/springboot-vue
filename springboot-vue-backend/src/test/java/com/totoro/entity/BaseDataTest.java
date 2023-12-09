package com.totoro.entity;

import com.totoro.entity.dto.Account;
import com.totoro.entity.vo.response.AuthorizeVo;
import com.totoro.service.AccountService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: totoro
 * @createDate: 2023 12 10 01 51
 * @description:
 **/
@SpringBootTest
class BaseDataTest {

    @Resource
    AccountService accountService;

    @Test
    public void test(){

        Account account = accountService.findAccountByNameOrEmail("test");

        AuthorizeVo viewObject = account.asViewObject(AuthorizeVo.class);

        System.out.println(viewObject);
    }

    @Test
    public void asViewObject(){

        Account account = accountService.findAccountByNameOrEmail("test");

        AuthorizeVo vo = account.asViewObject(AuthorizeVo.class, v -> {
            v.setExpire(new Date());
        });

        System.out.println(vo);

    }


}