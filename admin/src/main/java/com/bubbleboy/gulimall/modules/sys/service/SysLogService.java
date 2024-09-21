 

package com.bubbleboy.gulimall.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
  */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
