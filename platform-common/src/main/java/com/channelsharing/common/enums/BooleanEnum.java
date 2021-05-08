package com.channelsharing.common.enums;

/**
 * Created by liuhangjun on 2017/6/30.
 */
public enum BooleanEnum implements BaseEnum {

    yes(1, "yes"),

    no(0, "no");

    private Integer code;
    private String name;

    BooleanEnum(Integer code, String name){
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
