package com.channelsharing.hongqu.portal.api.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/1/24.
 */
public enum RoleType implements BaseEnum {

    user(1, "用户"), shopkeeper(2, "店主");

    private Integer code;
    private String name;

    RoleType(Integer code, String name){
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
