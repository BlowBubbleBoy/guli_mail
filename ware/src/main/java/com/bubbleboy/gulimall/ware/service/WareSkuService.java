package com.bubbleboy.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:22:32
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);
}

