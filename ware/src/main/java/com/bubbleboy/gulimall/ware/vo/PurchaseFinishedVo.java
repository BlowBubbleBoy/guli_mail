package com.bubbleboy.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseFinishedVo {
    private Long id;
    List<PurchaseItemVo> items;
}
