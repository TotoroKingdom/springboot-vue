package com.totoro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: totoro
 * @createDate: 2023 12 09 02 45
 * @description:
 **/
@RestController
@RequestMapping("api/test")
public class TestController {
    @GetMapping("hello")
    public String test(){
        return "hello world";
    }
}
