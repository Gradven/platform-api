package com.channelsharing.hongqu.supplier.api.enums;

import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/1/24.
 */
public enum SupplierUserStatus implements BaseEnum {

    forbidden(0, "未启用"), activated(1, "已启用");

    private Integer code;
    private String name;

    SupplierUserStatus(Integer code, String name){
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
