package com.channelsharing.pub.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/1/24.
 */
public enum OrderStatus implements BaseEnum {

    unPay(0, "未支付"), paid(1, "已支付"), completed(2, "已完成"), cancel(99, "已取消");

    private Integer code;
    private String name;

    OrderStatus(Integer code, String name){
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
