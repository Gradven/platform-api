package com.channelsharing.pub.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/1/24.
 */
public enum ApproveStatus implements BaseEnum {

    approving(1, "审核中"), approved(2, "审核通过"), notApproved(3, "审核不通过");

    private Integer code;
    private String name;

    ApproveStatus(Integer code, String name){
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
