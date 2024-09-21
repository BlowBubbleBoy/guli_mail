package com.bubbleboy.gulimall.product;


import com.bubbleboy.gulimall.product.entity.CategoryEntity;
import com.bubbleboy.gulimall.product.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductApplicationTests {

    @Autowired
    CategoryService categoryService;

    @Test
    public void contextLoads() {
        List<CategoryEntity> list = categoryService.list();
        for (CategoryEntity categoryEntity : list) {
            System.out.println(categoryEntity);
        }


    }

}
