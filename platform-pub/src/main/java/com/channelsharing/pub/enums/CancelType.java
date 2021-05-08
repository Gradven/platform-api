package com.channelsharing.pub.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2017/6/19.
 */
public enum CancelType implements BaseEnum {

     unCancel(0, "未取消订单"), userCancel(1, "用户主动取消"), outTimeCancel(2, "支付超时自动取消"), weixinPayFailedCancel(3, "支付失败系统取消");

    private Integer code;
    private String name;

    CancelType(Integer code, String name){
        this.code = code;
        this.name = name;

    }

    

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
