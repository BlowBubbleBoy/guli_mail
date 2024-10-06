package com.bubbleboy.gulimall.ware.feign;

import com.bubbleboy.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("product")
public interface ProductFeignService {

    @RequestMapping("/product/skuinfo/skuName/{skuId}")
    R skuName(@PathVariable("skuId") Long skuId);
}
