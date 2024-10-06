package com.bubbleboy.gulimall.product.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuEsModelTo {
    private Long skuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long categoryId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<AttrTo> attrs;

    @Data
    public static class AttrTo {
        private String attrId;
        private String attrName;
        private String attrValue;

    }
}
