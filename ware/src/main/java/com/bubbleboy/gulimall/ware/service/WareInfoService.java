package com.bubbleboy.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.ware.entity.WareInfoEntity;
import com.bubbleboy.gulimall.ware.vo.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * 仓库信息
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:22:32
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

