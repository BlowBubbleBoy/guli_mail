package com.bubbleboy.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.product.dao.CategoryDao;
import com.bubbleboy.gulimall.product.entity.CategoryEntity;
import com.bubbleboy.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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

    private List<CategoryEntity> getChildren(CategoryEntity root, Map<Long, List<CategoryEntity>> map) {

        List<CategoryEntity> children = map.getOrDefault(root.getCatId(), new ArrayList<>());

        for (CategoryEntity child : children) {
            child.setChildren(getChildren(child, map));
        }

        return children;
    }

}