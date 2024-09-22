package com.bubbleboy.gulimall.member.dao;

import com.bubbleboy.gulimall.member.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:20:49
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
