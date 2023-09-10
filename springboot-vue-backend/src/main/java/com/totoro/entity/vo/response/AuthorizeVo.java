package com.totoro.entity.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author: totoro
 * @createDate: 2023 09 10 23 44
 * @description:
 **/
@Data
public class AuthorizeVo {

    String username;

    String role;

    String token;

    Date expire;
}
