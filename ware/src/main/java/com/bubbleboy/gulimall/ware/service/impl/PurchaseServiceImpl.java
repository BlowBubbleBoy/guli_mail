package com.bubbleboy.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bubbleboy.gulimall.common.constant.WareConstant;
import com.bubbleboy.gulimall.ware.entity.PurchaseDetailEntity;
import com.bubbleboy.gulimall.ware.entity.WareSkuEntity;
import com.bubbleboy.gulimall.ware.service.PurchaseDetailService;
import com.bubbleboy.gulimall.ware.vo.PurchaseDetailMergeVo;
import com.bubbleboy.gulimall.ware.vo.PurchaseFinishedVo;
import com.bubbleboy.gulimall.ware.vo.PurchaseItemVo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.ware.dao.PurchaseDao;
import com.bubbleboy.gulimall.ware.entity.PurchaseEntity;
import com.bubbleboy.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    private final PurchaseDetailServiceImpl purchaseDetailService;
    private final WareSkuServiceImpl wareSkuService;

    public PurchaseServiceImpl(PurchaseDetailServiceImpl purchaseDetailService, WareSkuServiceImpl wareSkuService) {
        this.purchaseDetailService = purchaseDetailService;
        this.wareSkuService = wareSkuService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseEntity> unreceiveList() {

        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", WareConstant.PurchaseStatus.CREATED.getCode()).or().eq("status", WareConstant.PurchaseStatus.ALLOCATED.getCode());

        return this.list(queryWrapper);
    }

    @Override
    @Transactional
    public void purchaseDetailMerge(PurchaseDetailMergeVo purchaseDetailMergeVo) {
        Long purchaseId = purchaseDetailMergeVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatus.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        List<Long> items = purchaseDetailMergeVo.getItems();

        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntityList = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatus.ALLOCATED.getCode());
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(purchaseDetailEntityList);
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setStatus(WareConstant.PurchaseStatus.ALLOCATED.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }

    @Override
    @Transactional
    public void receivedPurchase(List<Long> ids) {
        List<PurchaseEntity> purchaseEntityList = ids.stream().map(obj -> {
            Collection<PurchaseEntity> purchaseEntities = this.listByIds(ids);
            return purchaseEntities.iterator().next();
        }).filter(item -> item.getStatus() == WareConstant.PurchaseStatus.CREATED.getCode() ||
                item.getStatus() == WareConstant.PurchaseStatus.ALLOCATED.getCode()
        ).map(i -> {
            i.setStatus(WareConstant.PurchaseStatus.RECEIVED.getCode());
            i.setUpdateTime(new Date());
            return i;
        }).collect(Collectors.toList());

        this.updateBatchById(purchaseEntityList);

        purchaseEntityList.forEach(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setPurchaseId(item.getId());
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatus.PURCHASING.getCode());
            purchaseDetailService.update(purchaseDetailEntity, new UpdateWrapper<PurchaseDetailEntity>().eq("purchase_id", purchaseDetailEntity.getPurchaseId()));
        });

    }


    @Override
    @Transactional
    public void purchaseFinished(PurchaseFinishedVo purchaseFinishedVo) {
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseFinishedVo.getId());
        purchaseEntity.setStatus(WareConstant.PurchaseStatus.FINISHED.getCode());

        boolean flag = true;
        List<PurchaseItemVo> items = purchaseFinishedVo.getItems();

        ArrayList<PurchaseDetailEntity> updates = new ArrayList<>();
        for (PurchaseItemVo item : items) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if (item.getStatue() == WareConstant.PurchaseDetailStatus.PURCHASEFAILED.getCode()) {
                flag = false;
                purchaseDetailEntity.setStatus(item.getStatue());
            } else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatus.FINISHED.getCode());
                PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(item.getItemId());

                wareSkuService.addStock(purchaseDetail.getSkuId(), purchaseDetail.getWareId(), purchaseDetail.getSkuNum());
            }

            purchaseDetailEntity.setId(item.getItemId());
            updates.add(purchaseDetailEntity);
        }
        purchaseDetailService.updateBatchById(updates);

        PurchaseEntity purchase = new PurchaseEntity();
        purchase.setId(purchaseFinishedVo.getId());
        purchase.setStatus(flag ? WareConstant.PurchaseStatus.FINISHED.getCode() : WareConstant.PurchaseStatus.HASERROR.getCode());
        purchase.setUpdateTime(new Date());
        this.updateById(purchase);

    }


}