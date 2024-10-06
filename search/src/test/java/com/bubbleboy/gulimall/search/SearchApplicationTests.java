package com.bubbleboy.gulimall.search;


import com.alibaba.fastjson.JSON;
import com.bubbleboy.gulimall.search.config.ElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchApplicationTests {


    @Autowired
    RestHighLevelClient client;

    @Test
    public void testESClient() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        User user1 = new User();
        user1.setName("张三");
        user1.setAge(13);
        user1.setGender(true);
        User user2 = new User();
        user2.setName("李四");
        user2.setAge(14);
        user2.setGender(false);
        String user1Json = JSON.toJSONString(user1);
        String user2Json = JSON.toJSONString(user2);
        indexRequest.source(user1Json, XContentType.JSON);
//        try {
//            //client.index(indexRequest, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("users");
        SearchResponse search = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(search);
        System.out.println(search.getHits());
        System.out.println(search.getHits().getTotalHits());
        System.out.println(search.getHits().getHits());
    }

}
@Data
class User{
    String name;
    Integer age;
    Boolean gender;
}
