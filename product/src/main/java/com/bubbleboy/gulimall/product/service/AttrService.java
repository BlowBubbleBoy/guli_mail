package com.bubbleboy.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.product.vo.AttrRespVo;
import com.bubbleboy.gulimall.product.vo.AttrVo;
import com.bubbleboy.gulimall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:18:13
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attrVo);

    PageUtils baseList(Map<String, Object> params, Long catalogId, String attrType);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttrInfo(AttrVo attr);

    List<AttrEntity> getAttrsRelation(Long attrGroupId);

    PageUtils getNoAttrRelation(Map<String, Object> params, Long attrGroupId);
}

