package com.bubbleboy.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.ware.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:22:32
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

