package com.bubbleboy.gulimall.product.dao;

import com.bubbleboy.gulimall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * 
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:18:13
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void updateSpuStatus(@Param("spuId") Long spuId, @Param("status") Integer status);
}
