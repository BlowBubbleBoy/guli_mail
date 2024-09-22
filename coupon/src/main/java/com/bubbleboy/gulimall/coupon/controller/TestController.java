package com.bubbleboy.gulimall.coupon.controller;

import com.bubbleboy.gulimall.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/coupon/test")
public class TestController {
    @Value("${coupon.username}")
    private String username;

    @Value("${coupon.password}")
    private String password;

    @GetMapping("/config")
    public R config(){
        return R.ok().put("username", username).put("password", password);
    }

}
