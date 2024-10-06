package com.bubbleboy.gulimall.product.controller;


import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.R;
import com.bubbleboy.gulimall.product.entity.AttrEntity;
import com.bubbleboy.gulimall.product.entity.ProductAttrValueEntity;
import com.bubbleboy.gulimall.product.service.impl.ProductAttrValueServiceImpl;
import com.bubbleboy.gulimall.product.vo.AttrRespVo;
import com.bubbleboy.gulimall.product.vo.AttrVo;
import com.bubbleboy.gulimall.product.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:18:13
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueServiceImpl productAttrValueService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/{attrType}/list/{catalogId}")
    //@RequiresPermissions("product:attr:list")
    public R baseList(@RequestParam Map<String, Object> params, @PathVariable("catalogId") Long catalogId, @PathVariable("attrType") String attrType) {

        PageUtils page = attrService.baseList(params, catalogId, attrType);

        return R.ok().put("page", page);

    }

    @GetMapping("/base/listforspu/{spuId}")
    public R baseListForSpu(@PathVariable("spuId") Long spuId) {

        List<ProductAttrValueEntity> data = productAttrValueService.baseListForSpu(spuId);

        return R.ok().put("data", data);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attrVo) {
        attrService.saveAttr(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr) {
        attrService.updateAttrInfo(attr);

        return R.ok();
    }

    @RequestMapping("/update/{spuId}")
    //@RequiresPermissions("product:attr:update")
    public R updateAttrValue( @PathVariable("spuId") Long spuId,@RequestBody List<ProductAttrValueEntity> entities) {
        productAttrValueService.updateAttrValue(spuId,entities);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
