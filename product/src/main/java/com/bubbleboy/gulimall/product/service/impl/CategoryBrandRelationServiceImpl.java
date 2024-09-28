package com.bubbleboy.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bubbleboy.gulimall.product.dao.BrandDao;
import com.bubbleboy.gulimall.product.dao.CategoryDao;
import com.bubbleboy.gulimall.product.entity.BrandEntity;


import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.product.dao.CategoryBrandRelationDao;
import com.bubbleboy.gulimall.product.entity.CategoryBrandRelationEntity;
import com.bubbleboy.gulimall.product.entity.CategoryEntity;
import com.bubbleboy.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CategoryDao categoryDao;

    private final CategoryBrandRelationDao categoryBrandRelationDao;

    public CategoryBrandRelationServiceImpl(CategoryBrandRelationDao categoryBrandRelationDao) {
        this.categoryBrandRelationDao = categoryBrandRelationDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        CategoryEntity categoryEntity = categoryDao.selectById(categoryBrandRelation.getCatelogId());
        BrandEntity brandEntity = brandDao.selectById(categoryBrandRelation.getBrandId());
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateCategoryNameById(Long catId, String name) {
        categoryBrandRelationDao.update(new CategoryBrandRelationEntity(), new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
    }

    @Override
    public void updateBrandNameById(Long brandId, String name) {
        categoryBrandRelationDao.updateBrandNameById(brandId, name);
    }

}