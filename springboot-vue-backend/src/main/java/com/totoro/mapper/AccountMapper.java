package com.totoro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.totoro.entity.dto.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: totoro
 * @createDate: 2023 12 09 23 09
 * @description:
 **/
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

}
