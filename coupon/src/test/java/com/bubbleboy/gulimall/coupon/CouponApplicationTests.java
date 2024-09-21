package com.bubbleboy.gulimall.coupon;

import com.bubbleboy.gulimall.common.utils.R;
import com.bubbleboy.gulimall.coupon.feign.TestFeignService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CouponApplicationTests {

    @Autowired
    TestFeignService feignService;

    @Test
    public void contextLoads() {

        R data = feignService.treeList();

        System.out.println(data.get("data"));



    }

}
