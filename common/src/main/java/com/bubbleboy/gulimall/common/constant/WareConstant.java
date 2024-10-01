package com.bubbleboy.gulimall.common.constant;

import lombok.Getter;

public class WareConstant {


    @Getter
    public enum PurchaseStatus {
        CREATED(0, "新建"), ALLOCATED(1, "已分配"), RECEIVED(2, "已领取"), FINISHED(3, "已完成"), HASERROR(4, "有异常");
        private final int code;
        private final String msg;

        PurchaseStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    @Getter
    public enum PurchaseDetailStatus {
        CREATED(0, "新建"), ALLOCATED(1, "已分配"), PURCHASING(2, "正在采购"), FINISHED(3, "已完成"), PURCHASEFAILED(4, "采购失败");
        private final int code;
        private final String msg;

        PurchaseDetailStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
