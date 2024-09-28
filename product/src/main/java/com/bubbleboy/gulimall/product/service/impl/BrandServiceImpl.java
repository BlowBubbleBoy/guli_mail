package com.bubbleboy.gulimall.product.service.impl;

import com.bubbleboy.gulimall.product.dao.CategoryBrandRelationDao;
import com.bubbleboy.gulimall.product.entity.CategoryBrandRelationEntity;
import com.bubbleboy.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.product.dao.BrandDao;
import com.bubbleboy.gulimall.product.entity.BrandEntity;
import com.bubbleboy.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> page;
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(key)) {
            wrapper.eq("brand_id", key).or().like("name", key).or().like("first_letter", key);
            page = this.page(new Query<BrandEntity>().getPage(params), wrapper);
        } else {
            page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    new QueryWrapper<>()
            );

        }
        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveDetail(BrandEntity brand) {
        this.updateById(brand);
        if (StringUtils.isNotBlank(brand.getName())){
            categoryBrandRelationService.updateBrandNameById(brand.getBrandId(),brand.getName());
        }
    }

}