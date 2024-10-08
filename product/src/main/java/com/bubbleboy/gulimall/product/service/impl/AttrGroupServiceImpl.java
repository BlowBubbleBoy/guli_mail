package com.bubbleboy.gulimall.product.service.impl;

import com.bubbleboy.gulimall.product.vo.AttrGroupAndAttrs;
import com.bubbleboy.gulimall.product.vo.AttrVo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.Query;

import com.bubbleboy.gulimall.product.dao.AttrGroupDao;
import com.bubbleboy.gulimall.product.entity.AttrEntity;
import com.bubbleboy.gulimall.product.entity.AttrGroupEntity;
import com.bubbleboy.gulimall.product.service.AttrGroupService;
import com.bubbleboy.gulimall.product.service.AttrService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrGroupDao attrGroupDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catalogId) {

        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        if (catalogId != 0) {
            wrapper = wrapper.eq("catalog_id", catalogId);
        }
        String key = (String) params.get("key");

        if (StringUtils.isNotBlank(key)) {
            wrapper.and((obj) -> obj.eq("attr_group_id", key).or().like("attr_group_name", key));
        }
        IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupAndAttrs> getAttrGroupWithAttrs(Long catalogId) {
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catalog_id", catalogId));

        return attrGroupEntities.stream().map((item) -> {
            AttrGroupAndAttrs attrGroupAndAttrs = new AttrGroupAndAttrs();
            BeanUtils.copyProperties(item, attrGroupAndAttrs);

            List<AttrEntity> attrs = attrService.getAttrsRelation(attrGroupAndAttrs.getAttrGroupId());
            List<AttrVo> attrVos = attrs.stream().map(obj -> {
                AttrVo attrVo = new AttrVo();
                BeanUtils.copyProperties(obj, attrVo);
                return attrVo;
            }).collect(Collectors.toList());
            attrGroupAndAttrs.setAttrs(attrVos);
            return attrGroupAndAttrs;
        }).collect(Collectors.toList());
    }

}