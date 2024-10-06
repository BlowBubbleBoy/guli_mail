package com.bubbleboy.gulimall.common.constant;


import lombok.Getter;

public class ProductConstant {

    @Getter
    public enum AttrEnum {
        ATTR_TYPE_BASE("1", "base", "基本属性"), ATTR_TYPE_SALE("0", "value", "销售属性");
        private final String code;
        private final String value;
        private final String desc;

        AttrEnum(String code, String value, String desc) {
            this.code = code;
            this.value = value;
            this.desc = desc;
        }

    }

    @Getter
    public enum SpuStatusEnum {
        CREATED(0, "新建"), AVAILABLE(1, "上架"), REMOVED(2, "下架");
        private final Integer code;
        private final String desc;

        SpuStatusEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }
}
