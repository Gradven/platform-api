package com.channelsharing.pub.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/1/24.
 */
public enum ShippingStatus implements BaseEnum {
    
    unShipped(0, "未发货"), shipped(1, "已发货"), signed(2, "已签收");

    private Integer code;
    private String name;

    ShippingStatus(Integer code, String name){
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
