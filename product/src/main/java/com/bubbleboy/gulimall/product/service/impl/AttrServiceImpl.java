package com.bubbleboy.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bubbleboy.gulimall.common.constant.ProductConstant;
import com.bubbleboy.gulimall.product.controller.vo.AttrRespVo;
import com.bubbleboy.gulimall.product.controller.vo.AttrVo;
import com.bubbleboy.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.bubbleboy.gulimall.product.dao.AttrGroupDao;
import com.bubbleboy.gulimall.product.dao.CategoryDao;
import com.bubbleboy.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.bubbleboy.gulimall.product.entity.AttrGroupEntity;
import com.bubbleboy.gulimall.product.entity.CategoryEntity;
import com.bubbleboy.gulimall.product.service.AttrAttrgroupRelationService;
import com.bubbleboy.gulimall.product.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.product.dao.AttrDao;
import com.bubbleboy.gulimall.product.entity.AttrEntity;
import com.bubbleboy.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.save(attrEntity);
        if (ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode().equals(attrVo.getAttrType().toString()) && attrVo.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity attrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());

            attrAttrgroupRelationService.save(attrgroupRelationEntity);
        }
    }

    @Override
    public PageUtils baseList(Map<String, Object> params, Long catelogId, String attrType) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("attr_type",
                ProductConstant.AttrEnum.ATTR_TYPE_BASE.getValue().equalsIgnoreCase(attrType) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if (catelogId != 0) {
            wrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.eq("attr_id", key).or().like("attr_name", key);
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        List<AttrEntity> records = page.getRecords();
        PageUtils pageUtils = new PageUtils(page);
        List<AttrRespVo> data = records.stream().map(record -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(record, attrRespVo);
            CategoryEntity category = categoryDao.selectById(record.getCatelogId());
            if (ProductConstant.AttrEnum.ATTR_TYPE_BASE.getValue().equals(attrType)) {
                AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", record.getAttrId()));
                if (relationEntity != null && relationEntity.getAttrGroupId() != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            if (category != null) {
                attrRespVo.setCatelogName(category.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(data);

        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attr = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        if (attr == null) {
            return attrRespVo;
        }

        BeanUtils.copyProperties(attr, attrRespVo);
        List<Long> catelogPath = categoryService.getCatelogPath(attrRespVo.getCatelogId());
        if (ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode().equals(attr.getAttrType().toString())) {
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (relationEntity != null && relationEntity.getAttrGroupId() != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName() == null ? "" : attrGroupEntity.getAttrGroupName());
                attrRespVo.setAttrGroupId(relationEntity.getAttrGroupId() == null ? null : relationEntity.getAttrGroupId());
            }
        }

        attrRespVo.setCatelogPath(catelogPath);

        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttrInfo(AttrVo attrvo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrvo, attrEntity);
        this.updateById(attrEntity);
        if (ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode().equals(attrvo.getAttrType().toString())) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attrvo.getAttrId());
            relationEntity.setAttrGroupId(attrvo.getAttrGroupId());
            Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrvo.getAttrId()));
            if (count > 0) {
                attrAttrgroupRelationDao.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrvo.getAttrId()));
            } else {
                attrAttrgroupRelationDao.insert(relationEntity);
            }
        }
    }

    @Override
    public List<AttrEntity> getAttrsRelation(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> relations = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));

        if (relations.isEmpty()) {
            return null;
        }
        List<Long> attrIds = relations.stream().map(AttrAttrgroupRelationEntity::getAttrId
        ).collect(Collectors.toList());

        return (List<AttrEntity>) this.listByIds(attrIds);
    }

    @Override
    public PageUtils getNoAttrRelation(Map<String, Object> params, Long attrGroupId) {
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", attrGroupEntity.getCatelogId()));
        List<Long> collect = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        List<AttrAttrgroupRelationEntity> attrInGroupList = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));

        List<Long> attrIds = attrInGroupList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", attrGroupEntity.getCatelogId()).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(obj -> obj.eq("attr_id", key).or().like("attr_name", key));
        }
        if (attrIds != null && !attrIds.isEmpty()) {
            wrapper.notIn("attr_id", attrIds);
        }

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

}