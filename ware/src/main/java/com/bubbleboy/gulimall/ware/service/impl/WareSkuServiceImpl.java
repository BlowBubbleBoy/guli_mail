package com.bubbleboy.gulimall.ware.service.impl;

import com.bubbleboy.gulimall.common.utils.R;
import com.bubbleboy.gulimall.ware.entity.WareInfoEntity;
import com.bubbleboy.gulimall.ware.feign.ProductFeignService;
import com.bubbleboy.gulimall.ware.vo.SkuHasStockVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.ware.dao.WareSkuDao;
import com.bubbleboy.gulimall.ware.entity.WareSkuEntity;
import com.bubbleboy.gulimall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Service("wareSkuService")
@Slf4j
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    private final WareSkuDao wareSkuDao;
    @Autowired
    private ProductFeignService productFeignService;

    public WareSkuServiceImpl(WareSkuDao wareSkuDao) {
        this.wareSkuDao = wareSkuDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();

        String wareId = (String) params.get("wareId");
        String skuId = (String) params.get("skuId");

        if (wareId != null && StringUtils.isNotBlank(wareId)) {
            wrapper.eq("ware_id", wareId);
        }
        if (skuId != null && StringUtils.isNotBlank(skuId)) {
            wrapper.eq("sku_id", skuId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void addStock(Long skuId, Long wareId, Integer skuNum) {

        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (wareSkuEntities.isEmpty()) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            try {
                R r = productFeignService.skuName(skuId);
                if (r.getCode() == 0) {
                    wareSkuEntity.setSkuName((String) r.get("skuName"));
                }
            } catch (RuntimeException e) {
                log.error(e.getMessage(), e);
            }
            wareSkuDao.insert(wareSkuEntity);
        }

        wareSkuDao.addStock(skuId, wareId, skuNum);
    }


    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        List<WareSkuEntity> wareInfoEntities = list(new QueryWrapper<WareSkuEntity>().in("sku_id", skuIds));
        return wareInfoEntities.stream().map(item -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            skuHasStockVo.setSkuId(item.getSkuId());
            Integer stock = item.getStock();
            skuHasStockVo.setHasStock(stock != null && stock - item.getStockLocked() > 0);
            return skuHasStockVo;
        }).collect(Collectors.toList());
    }

}