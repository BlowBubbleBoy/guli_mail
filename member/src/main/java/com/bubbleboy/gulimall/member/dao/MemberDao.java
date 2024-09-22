package com.bubbleboy.gulimall.member.dao;

import com.bubbleboy.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:20:49
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
