package com.bubbleboy.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.product.dao.ProductAttrValueDao;
import com.bubbleboy.gulimall.product.entity.ProductAttrValueEntity;
import com.bubbleboy.gulimall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveProductAttr(List<ProductAttrValueEntity> productAttrValueEntities) {
        this.saveBatch(productAttrValueEntities);
    }

    @Override
    public List<ProductAttrValueEntity> baseListForSpu(Long spuId) {
        return list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
    }

    @Override
    @Transactional
    public void updateAttrValue(Long spuId, List<ProductAttrValueEntity> entities) {
        remove(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        entities.forEach(entity -> entity.setSpuId(spuId));
        saveBatch(entities);
    }

}