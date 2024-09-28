package com.bubbleboy.gulimall.coupon.feign;

import com.bubbleboy.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("product")
public interface TestFeignService {

    @GetMapping("/product/category/list/tree")
    R treeList();
}
