package com.bubbleboy.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bubbleboy.gulimall.common.constant.ProductConstant;
import com.bubbleboy.gulimall.common.to.SkuReductionTo;
import com.bubbleboy.gulimall.common.to.SpuBoundTo;
import com.bubbleboy.gulimall.common.utils.R;
import com.bubbleboy.gulimall.product.entity.*;
import com.bubbleboy.gulimall.product.feign.CouponFeignService;
import com.bubbleboy.gulimall.product.feign.SearchFeignService;
import com.bubbleboy.gulimall.product.feign.WareFeignService;
import com.bubbleboy.gulimall.product.service.SpuInfoDescService;
import com.bubbleboy.gulimall.product.to.SkuEsModelTo;
import com.bubbleboy.gulimall.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.product.dao.SpuInfoDao;
import com.bubbleboy.gulimall.product.service.SpuInfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private SpuImagesServiceImpl spuImagesService;
    @Autowired
    private AttrServiceImpl attrService;
    @Autowired
    private ProductAttrValueServiceImpl productAttrValueService;
    @Autowired
    private SkuInfoServiceImpl skuInfoService;
    @Autowired
    private SkuImagesServiceImpl skuImagesService;
    @Autowired
    private SkuSaleAttrValueServiceImpl skuSaleAttrValueService;
    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private BrandServiceImpl brandService;
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private WareFeignService wareFeignService;
    @Autowired
    private SearchFeignService searchFeignService;
    @Autowired
    private SpuInfoDao spuInfoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());

        this.saveBaseSpuInfo(spuInfoEntity);

        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", spuSaveVo.getDecript()));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        spuImagesService.saveSpuImages(spuInfoEntity.getId(), spuSaveVo.getImages());

        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            productAttrValueEntity.setAttrId(attr.getAttrId());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(productAttrValueEntities);

        Bounds bound = spuSaveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bound, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R result = couponFeignService.saveSpuBounds(spuBoundTo);
        if (result.getCode() != 0) {
            log.error("远程保存spu信息失败！");
        }


        List<Skus> skus = spuSaveVo.getSkus();
        skus.forEach(sku -> {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(sku, skuInfoEntity);
            List<Images> images = sku.getImages();
            String defaultImageUrl = null;
            for (Images image : images) {
                if (image.getDefaultImg() == 1) {
                    defaultImageUrl = image.getImgUrl();
                    break;
                }
            }
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            skuInfoEntity.setSaleCount(0L);
            skuInfoEntity.setSkuDefaultImg(defaultImageUrl);

            skuInfoService.saveSkuInfo(skuInfoEntity);

            Long skuId = skuInfoEntity.getSkuId();

            List<SkuImagesEntity> skuImageEntity = sku.getImages().stream().map(img -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setSkuId(skuId);
                skuImagesEntity.setImgUrl(img.getImgUrl());
                skuImagesEntity.setDefaultImg(img.getDefaultImg());
                return skuImagesEntity;
            }).filter(obj -> StringUtils.isNotBlank(obj.getImgUrl())).collect(Collectors.toList());

            skuImagesService.saveBatch(skuImageEntity);

            List<Attr> attrs = sku.getAttr();

            List<SkuSaleAttrValueEntity> saleAttrValues = attrs.stream().map(attr -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(skuId);
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());

            skuSaleAttrValueService.saveAttrValues(saleAttrValues);

            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(sku, skuReductionTo);
            if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) > 0) {
                R r = couponFeignService.saveSkuRedutionInfo(skuReductionTo);

                if (r.getCode() != 0) {
                    log.error("远程保存sku优惠信息失败！");
                }
            }

        });


    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.save(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String status = (String) params.get("status");
        if (null != status && StringUtils.isNotBlank((status))) {
            wrapper.eq("publish_status", params.get("status"));
        }
        String brandId = (String) params.get("brandId");
        if (null != brandId && !"0".equals(brandId)) {
            wrapper.eq("brand_id", params.get("brandId"));
        }
        String categoryId = (String) params.get("categoryId");
        if (null != categoryId && !"0".equals(categoryId)) {
            wrapper.eq("category_id", params.get("categoryId"));
        }
        String key = (String) params.get("key");

        if (null != key && StringUtils.isNotBlank((key))) {
            wrapper.and(w -> w.eq("id", params.get("key")).or().like("spu_name", params.get("key")));
        }
        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public void productListing(Long spuId) {
        List<SkuEsModelTo> skuEsModelToList = new ArrayList<>();

        List<ProductAttrValueEntity> productAttrValueEntityList = productAttrValueService.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        List<Long> attrIds = productAttrValueEntityList.stream().map(item -> item.getAttrId()).collect(Collectors.toList());
        List<Long> searchAttrIds = attrService.getSearchAttrIds(attrIds);

        HashSet<Long> searchAttrIdsSet = new HashSet<>(searchAttrIds);

        List<SkuEsModelTo.AttrTo> attrTos = productAttrValueEntityList.stream()
                .filter(i -> searchAttrIdsSet.contains(i.getAttrId()))
                .map(o -> {
                    SkuEsModelTo.AttrTo attrTo = new SkuEsModelTo.AttrTo();
                    attrTo.setAttrId(String.valueOf(o.getAttrId()));
                    attrTo.setAttrName(o.getAttrName());
                    attrTo.setAttrValue(o.getAttrValue());
                    return attrTo;
                })
                .collect(Collectors.toList());


        List<SkuInfoEntity> skuInfoList = skuInfoService.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        Long brandId = null;
        Long catId = null;

        List<Long> skuIds = new ArrayList<>();
        if (!skuInfoList.isEmpty()) {
            brandId = skuInfoList.get(0).getBrandId();
            catId = skuInfoList.get(0).getCatalogId();
            skuIds = skuInfoList.stream().map(skuInfo -> skuInfo.getSkuId()).collect(Collectors.toList());
        }

        SkuHasStockVo stockVo;
        Map<Long, Boolean> hasStockMap = new HashMap<>();
        try {
            R r = wareFeignService.getSkuHasStock(skuIds);
            if (r.getCode() == 0) {
                List<LinkedHashMap> data = (List<LinkedHashMap>) r.get("data");
                hasStockMap = data.stream()
                        .collect(Collectors.toMap(
                                item -> Long.parseLong(item.get("skuId").toString()),
                                item -> Boolean.parseBoolean(item.get("hasStock").toString())
                        ));
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }


        BrandEntity brandEntity = brandService.getById(brandId);
        CategoryEntity categoryEntity = categoryService.getById(catId);

        Map<Long, Boolean> finalHasStockMap = hasStockMap;
        List<SkuEsModelTo> skuEsModelTos = skuInfoList.stream().map(skuInfo -> {
            SkuEsModelTo skuEsModelTo = new SkuEsModelTo();
            skuEsModelTo.setSkuId(skuInfo.getSkuId());
            skuEsModelTo.setSkuTitle(skuInfo.getSkuTitle());
            skuEsModelTo.setSkuPrice(skuInfo.getPrice());
            skuEsModelTo.setSkuImg(skuInfo.getSkuDefaultImg());
            skuEsModelTo.setHasStock(finalHasStockMap.get(skuInfo.getSkuId()) != null && finalHasStockMap.get(skuInfo.getSkuId()));
            skuEsModelTo.setHotScore(0L);
            skuEsModelTo.setSaleCount(skuInfo.getSaleCount());
            skuEsModelTo.setCategoryId(skuInfo.getCatalogId());
            skuEsModelTo.setCatalogName(categoryEntity.getName());
            skuEsModelTo.setBrandId(skuInfo.getBrandId());
            skuEsModelTo.setBrandName(brandEntity.getName());
            skuEsModelTo.setBrandImg(brandEntity.getLogo());
            skuEsModelTo.setAttrs(attrTos);

            return skuEsModelTo;
        }).collect(Collectors.toList());
        R result = null;
        try {
            result = searchFeignService.productStatusUp(skuEsModelTos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (result.getCode() == 0) {
            spuInfoDao.updateSpuStatus(spuId, ProductConstant.SpuStatusEnum.AVAILABLE.getCode());
        } else {

        }

    }

}