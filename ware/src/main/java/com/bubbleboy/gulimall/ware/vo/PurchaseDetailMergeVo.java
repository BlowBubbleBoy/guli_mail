package com.bubbleboy.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseDetailMergeVo {
    private Long purchaseId;
    private List<Long> items;
}
