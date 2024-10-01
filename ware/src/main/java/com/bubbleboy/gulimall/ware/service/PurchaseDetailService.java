package com.bubbleboy.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.ware.entity.PurchaseDetailEntity;
import com.bubbleboy.gulimall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:22:32
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);


}

