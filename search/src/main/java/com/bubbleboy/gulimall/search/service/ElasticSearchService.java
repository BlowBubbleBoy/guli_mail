package com.bubbleboy.gulimall.search.service;

import com.bubbleboy.gulimall.search.to.SkuEsModelTo;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchService {
    Boolean productStatusUp(List<SkuEsModelTo> skuEsModelTos) throws IOException;
}
