package com.bubbleboy.gulimall.product.service.impl;

import com.bubbleboy.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.product.dao.CategoryDao;
import com.bubbleboy.gulimall.product.entity.CategoryEntity;
import com.bubbleboy.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    private final CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listTree() {

        List<CategoryEntity> categoryList = categoryDao.selectList(null);

        Map<Long, List<CategoryEntity>> map = categoryList.stream()
                .collect(Collectors.groupingBy(CategoryEntity::getParentCid));

        categoryList.sort(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())));


        List<CategoryEntity> rootList = map.getOrDefault(0L, new ArrayList<>());

        for (CategoryEntity root : rootList) {
            root.setChildren(getChildren(root, map));
        }

        return rootList;
    }

    @Override
    public void deleteByIds(List<Long> catIds) {
        categoryDao.deleteBatchIds(catIds);
    }

    @Override
    public List<Long> getCatelogPath(Long catelogId) {
        ArrayList<Long> list = new ArrayList<>();
        CategoryEntity category = this.getById(catelogId);
        this.getCatelogPath(category, list);
        Collections.reverse(list);
        return list;
    }

    @Override
    @Transactional
    public void saveDetail(CategoryEntity category) {
        this.updateById(category);

        if (StringUtils.isNotBlank(category.getName())){
            categoryBrandRelationService.updateCategoryNameById(category.getCatId(),category.getName());
        }
    }

    private void getCatelogPath(CategoryEntity category, ArrayList<Long> list) {
        list.add(category.getCatId());
        if (category.getParentCid() != 0) {
            CategoryEntity categoryEntity = this.getById(category.getParentCid());
            this.getCatelogPath(categoryEntity, list);
        }
    }

    private List<CategoryEntity> getChildren(CategoryEntity root, Map<Long, List<CategoryEntity>> map) {

        List<CategoryEntity> children = map.getOrDefault(root.getCatId(), new ArrayList<>());

        for (CategoryEntity child : children) {
            child.setChildren(getChildren(child, map));
        }

        return children;
    }

}