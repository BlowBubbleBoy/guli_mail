package com.bubbleboy.gulimall.search.controller;

import com.bubbleboy.gulimall.common.exception.ExcCodeEnum;
import com.bubbleboy.gulimall.common.utils.R;
import com.bubbleboy.gulimall.search.service.ElasticSearchService;
import com.bubbleboy.gulimall.search.to.SkuEsModelTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSearchController {
    @Autowired
    private ElasticSearchService elasticSearchService;

    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModelTo> skuEsModelTos) {
        Boolean flag = false;
        try {
            flag = elasticSearchService.productStatusUp(skuEsModelTos);
        } catch (IOException e) {
            log.error("ES商品上架错误: {}", e);

        }
        if (flag) {
            return R.error(ExcCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), ExcCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        return R.ok();

    }
}
