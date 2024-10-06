package com.bubbleboy.gulimall.common.exception;

import lombok.Getter;

@Getter
public enum ExcCodeEnum {
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常"),
    ARGUMENT_VALID_EXCEPTION(10001, "参数校验异常");
    private final int code;
    private final String msg;

    ExcCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
