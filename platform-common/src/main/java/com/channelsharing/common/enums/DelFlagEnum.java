package com.channelsharing.common.enums;

/**
 * Created by liuhangjun on 2018/3/7.
 */
public enum DelFlagEnum {

    deleted(1, "deleted"),

    notDelete(0, "notDelete");

    private Integer code;
    private String name;

    DelFlagEnum(Integer code, String name){
        this.code = code;
        this.name = name;

    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
