package com.bubbleboy.gulimall.coupon.service.impl;

import com.bubbleboy.gulimall.common.to.MemberPriceTo;
import com.bubbleboy.gulimall.common.to.SkuReductionTo;
import com.bubbleboy.gulimall.coupon.entity.MemberPriceEntity;
import com.bubbleboy.gulimall.coupon.entity.SkuLadderEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.coupon.dao.SkuFullReductionDao;
import com.bubbleboy.gulimall.coupon.entity.SkuFullReductionEntity;
import com.bubbleboy.gulimall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    private final SkuLadderServiceImpl skuLadderService;
    private final MemberPriceServiceImpl memberPriceService;

    public SkuFullReductionServiceImpl(SkuLadderServiceImpl skuLadderService, MemberPriceServiceImpl memberPriceService) {
        this.skuLadderService = skuLadderService;
        this.memberPriceService = memberPriceService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuRedutionInfo(SkuReductionTo skuReductionTo) {

        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTo, skuLadderEntity);
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());

        if (skuReductionTo.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }

        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        if ((skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) > 0)) {
            this.save(skuFullReductionEntity);
        }

        List<MemberPriceTo> memberPriceToList = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntityList = memberPriceToList.stream().map(obj -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberLevelId(obj.getId());
            memberPriceEntity.setMemberLevelName(obj.getName());
            memberPriceEntity.setMemberPrice(obj.getPrice());
            memberPriceEntity.setAddOther(1);

            return memberPriceEntity;
        }).filter(obj -> (obj).getMemberPrice().compareTo(new BigDecimal(0)) > 0).collect(Collectors.toList());

        memberPriceService.saveBatch(memberPriceEntityList);

    }

}