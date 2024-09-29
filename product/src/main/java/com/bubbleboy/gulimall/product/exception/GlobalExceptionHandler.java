package com.bubbleboy.gulimall.product.exception;

import com.bubbleboy.gulimall.common.exception.ExcCodeEnum;
import com.bubbleboy.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestControllerAdvice(basePackages = "com.bubbleboy.gulimall.product.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R argumentNotValidException(MethodArgumentNotValidException e) {

        Map<String, String> respMap = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();

        bindingResult.getFieldErrors().forEach((item)-> respMap.put(item.getField(), item.getDefaultMessage()));

        return R.error(ExcCodeEnum.ARGUMENT_VALID_EXCEPTION.getCode(),ExcCodeEnum.ARGUMENT_VALID_EXCEPTION.getMsg()).put("data", respMap);
    }

    @ExceptionHandler(value = Exception.class)
    public R exception(Exception e) {

        log.error(e.getMessage());
        return R.error(ExcCodeEnum.UNKNOWN_EXCEPTION.getCode(),ExcCodeEnum.UNKNOWN_EXCEPTION.getMsg()).put("data", e.getMessage());
    }
}
