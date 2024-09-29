package com.bubbleboy.gulimall.product.feign;


import com.bubbleboy.gulimall.common.to.SkuReductionTo;
import com.bubbleboy.gulimall.common.to.SpuBoundTo;
import com.bubbleboy.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("coupon")
public interface CouponFeignService {

    @PostMapping("/coupon/spubounds/saveSpuBounds")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveSkuRedutionInfo")
    R saveSkuRedutionInfo(@RequestBody SkuReductionTo skuReductionTo);
}
