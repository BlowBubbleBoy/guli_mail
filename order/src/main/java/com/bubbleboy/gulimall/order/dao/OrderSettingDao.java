package com.bubbleboy.gulimall.order.dao;

import com.bubbleboy.gulimall.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:17:15
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
