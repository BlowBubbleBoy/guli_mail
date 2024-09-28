package com.bubbleboy.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.product.entity.BrandEntity;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:18:13
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(@Valid BrandEntity brand);

}

