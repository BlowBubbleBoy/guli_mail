package com.bubbleboy.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:18:13
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuImages(Long id, List<String> images);
}

