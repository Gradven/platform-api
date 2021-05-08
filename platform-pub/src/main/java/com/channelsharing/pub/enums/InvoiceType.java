package com.channelsharing.pub.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/1/24.
 */
public enum InvoiceType implements BaseEnum {

    normal(1, "普通发票"), paid(1, "增值税发票");

    private Integer code;
    private String name;

    InvoiceType(Integer code, String name){
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
