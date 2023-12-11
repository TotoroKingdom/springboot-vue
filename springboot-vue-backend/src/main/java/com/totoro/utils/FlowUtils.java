package com.totoro.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: totoro
 * @createDate: 2023 12 11 17 15
 * @description:
 **/
@Component
public class FlowUtils {

    @Resource( name = "stringRedisTemplate")
    StringRedisTemplate redisTemplate;

    public boolean limitOnceCheck(String key, int blockTime){
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            return false;
        }else {
            redisTemplate.opsForValue().set(key,"",blockTime, TimeUnit.SECONDS);
            return true;
        }

    }
}
