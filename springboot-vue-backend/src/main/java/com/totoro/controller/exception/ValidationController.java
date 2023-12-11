package com.totoro.controller.exception;

import com.totoro.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: totoro
 * @createDate: 2023 12 12 04 58
 * @description:
 **/
@Slf4j
@RestControllerAdvice
public class ValidationController {

    @ExceptionHandler(ValidationException.class)
    public RestBean<Void> validateException(ValidationException exception){
        log.warn("Resolve[{}:{}]",exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(400,"请求参数有误");
    }
}
