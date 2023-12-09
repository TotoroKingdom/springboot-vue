package com.totoro.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.totoro.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author: totoro
 * @createDate: 2023 12 09 23 00
 * @description:
 **/
@Data
@TableName("db_account")
@AllArgsConstructor
public class Account implements BaseData {

    @TableId(type = IdType.AUTO)
    Integer id;

    String username;

    String password;

    String email;

    String role;

    Date registerTime;

}
