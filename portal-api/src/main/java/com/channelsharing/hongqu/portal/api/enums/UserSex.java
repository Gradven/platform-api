package com.channelsharing.hongqu.portal.api.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/1/24.
 */
public enum UserSex implements BaseEnum {

    unknow(0, "未知"), male(1, "男"), female(2, "女");

    private Integer code;
    private String name;

    UserSex(Integer code, String name){
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
