package com.bubbleboy.gulimall.coupon.dao;

import com.bubbleboy.gulimall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:20:07
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
