package com.bubbleboy.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.bubbleboy.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bubbleboy.gulimall.product.entity.SpuInfoEntity;
import com.bubbleboy.gulimall.product.service.SpuInfoService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.R;



/**
 * spu信息
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:18:13
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:spuinfo:list")
    public R queryPageByCondition(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody SpuSaveVo spuSaveVo){
		spuInfoService.saveSpuInfo(spuSaveVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    @PostMapping("/{spuId}/up")
    //@RequiresPermissions("product:spuinfo:list")
    public R productListing(@PathVariable("spuId") Long spuId){
        spuInfoService.productListing(spuId);

        return R.ok();
    }
}
