package com.bubbleboy.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.ware.entity.PurchaseEntity;
import com.bubbleboy.gulimall.ware.vo.PurchaseDetailMergeVo;
import com.bubbleboy.gulimall.ware.vo.PurchaseFinishedVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:22:32
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseEntity> unreceiveList();

    void purchaseDetailMerge(PurchaseDetailMergeVo purchaseDetailMergeVo);

    void receivedPurchase(List<Long> ids);

    void purchaseFinished(PurchaseFinishedVo purchaseFinishedVo);
}

