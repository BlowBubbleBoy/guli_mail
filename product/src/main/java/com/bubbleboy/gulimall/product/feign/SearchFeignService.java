package com.bubbleboy.gulimall.product.feign;

import com.bubbleboy.gulimall.common.utils.R;
import com.bubbleboy.gulimall.product.to.SkuEsModelTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModelTo> skuEsModelTos);
}
