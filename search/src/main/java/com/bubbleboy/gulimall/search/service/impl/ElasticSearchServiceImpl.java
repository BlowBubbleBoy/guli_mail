package com.bubbleboy.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.bubbleboy.gulimall.search.config.ElasticSearchConfig;
import com.bubbleboy.gulimall.search.constant.EsConstant;
import com.bubbleboy.gulimall.search.service.ElasticSearchService;
import com.bubbleboy.gulimall.search.to.SkuEsModelTo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService {
    @Autowired
    private RestHighLevelClient client;

    @Override
    public Boolean productStatusUp(List<SkuEsModelTo> skuEsModelTos) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModelTo esModelTo : skuEsModelTos) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(esModelTo.getSkuId().toString());
            String jsonString = JSON.toJSONString(esModelTo);
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = client.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);

        Boolean errorFlag = bulk.hasFailures();
        List<String> itemIds = Arrays.stream(bulk.getItems()).map(item -> item.getId()).collect(Collectors.toList());
        if (errorFlag) {
            log.error("商品商家错误:{}", itemIds);
        }
        return errorFlag;
    }
}
