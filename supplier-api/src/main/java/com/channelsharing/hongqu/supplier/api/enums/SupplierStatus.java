package com.channelsharing.hongqu.supplier.api.enums;

import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/1/30.
 */
public enum SupplierStatus implements BaseEnum {

    inreviewed(0, "审核中"), approved(1, "审核通过"), unapprove(2, "审核不通过");

    private Integer code;
    private String name;

    SupplierStatus(Integer code, String name){
        this.code = code;
        this.name = name;

    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
