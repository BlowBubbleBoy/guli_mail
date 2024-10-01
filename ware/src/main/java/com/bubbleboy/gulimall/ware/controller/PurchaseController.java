package com.bubbleboy.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.bubbleboy.gulimall.ware.vo.PurchaseDetailMergeVo;
import com.bubbleboy.gulimall.ware.vo.PurchaseFinishedVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bubbleboy.gulimall.ware.entity.PurchaseEntity;
import com.bubbleboy.gulimall.ware.service.PurchaseService;
import com.bubbleboy.gulimall.common.utils.PageUtils;
import com.bubbleboy.gulimall.common.utils.R;


/**
 * 采购信息
 *
 * @author bubbleboy
 * @email lsc11400@163.com
 * @date 2024-09-21 21:22:32
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    @PostMapping("/done")
    public R purchaseFinished(@RequestBody PurchaseFinishedVo purchaseFinishedVo) {
        purchaseService.purchaseFinished(purchaseFinishedVo);
        return R.ok();
    }


    @GetMapping("/unreceive/list")
    public R unreceiveList() {
        List<PurchaseEntity> purchaseEntities = purchaseService.unreceiveList();

        return R.ok().put("data", purchaseEntities);
    }

    @PostMapping("/merge")
    //@RequiresPermissions("ware:purchase:list")
    public R purchaseDetailMerge(@RequestBody PurchaseDetailMergeVo purchaseDetailMergeVo) {
        purchaseService.purchaseDetailMerge(purchaseDetailMergeVo);

        return R.ok();
    }

    @PostMapping("/received")
    public R receivedPurchase(@RequestBody List<Long> ids) {
        purchaseService.receivedPurchase(ids);

        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
