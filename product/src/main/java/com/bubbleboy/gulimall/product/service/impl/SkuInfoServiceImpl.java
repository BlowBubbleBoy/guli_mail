package com.bubbleboy.gulimall.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.product.dao.SkuInfoDao;
import com.bubbleboy.gulimall.product.entity.SkuInfoEntity;
import com.bubbleboy.gulimall.product.service.SkuInfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.save(skuInfoEntity);
    }

    @Override
    public PageUtils getListByCondition(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        String brandId = (String) params.get("brandId");
        if (null != brandId && !"0".equals(brandId)) {
            wrapper.eq("brand_id", params.get("brandId"));
        }
        String categoryId = (String) params.get("categoryId");
        if (null != categoryId && !"0".equals(categoryId)) {
            wrapper.eq("category_id", params.get("categoryId"));
        }
        String key = (String) params.get("key");

        if (null != key && StringUtils.isNotBlank((key))) {
            wrapper.and(w -> w.eq("sku_id", params.get("key")).or().like("sku_name", params.get("key")));
        }
        String min = (String) params.get("min");
        String max = (String) params.get("max");

        if (!("0".equals(min) && "0".equals(max))) {
            if (null != min) {
                wrapper.ge("price", params.get("min"));
            }

            if (null != max) {
                wrapper.le("price", params.get("max"));
            }


        }

        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

}