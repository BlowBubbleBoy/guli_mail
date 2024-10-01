package com.bubbleboy.gulimall.ware.service.impl;

import com.bubbleboy.gulimall.ware.entity.PurchaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.ware.dao.PurchaseDetailDao;
import com.bubbleboy.gulimall.ware.entity.PurchaseDetailEntity;
import com.bubbleboy.gulimall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        String wareId = (String) params.get("wareId");
        String status = (String) params.get("status");
        String key = (String) params.get("key");

        if (wareId != null && StringUtils.isNotBlank(wareId)) {
            wrapper.eq("ware_id", wareId);
        }
        if (status != null && StringUtils.isNotBlank(status)) {
            wrapper.eq("status", status);
        }
        if (key != null && StringUtils.isNotBlank(key)) {
            wrapper.and((w) -> {
                w.eq("id", key).or().eq("purchase_id", key).or().eq("sku_id", key);
            });
        }

        IPage<PurchaseDetailEntity> page = this.page(new Query<PurchaseDetailEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }


}