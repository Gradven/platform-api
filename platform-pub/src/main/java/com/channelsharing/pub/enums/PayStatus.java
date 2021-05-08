package com.channelsharing.pub.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/1/24.
 */
public enum PayStatus implements BaseEnum {

    unPay(0, "未支付"), paid(1, "已支付");

    private Integer code;
    private String name;

    PayStatus(Integer code, String name){
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
