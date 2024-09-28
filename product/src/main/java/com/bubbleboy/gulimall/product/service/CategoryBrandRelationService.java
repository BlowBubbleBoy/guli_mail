package com.bubbleboy.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.product.entity.CategoryBrandRelationEntity;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:18:13
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    void updateCategoryNameById(Long catId, String name);

    void updateBrandNameById(Long brandId, @NotBlank(message = "品牌名不能为空") String name);

    List<CategoryBrandRelationEntity> getBrandListBycatId(Long catId);
}

