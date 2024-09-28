package com.bubbleboy.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.bubbleboy.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.bubbleboy.gulimall.product.service.AttrAttrgroupRelationService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    private final AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    public AttrAttrgroupRelationServiceImpl(AttrAttrgroupRelationDao attrAttrgroupRelationDao) {
        this.attrAttrgroupRelationDao = attrAttrgroupRelationDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteRelations(List<AttrAttrgroupRelationEntity> entities) {

        attrAttrgroupRelationDao.deleteRelations(entities);
    }

    @Override
    @Transactional
    public void saveAttrGroupRelations(List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities) {
        this.saveBatch(attrAttrgroupRelationEntities);
    }

}