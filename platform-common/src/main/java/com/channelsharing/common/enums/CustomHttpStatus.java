package com.channelsharing.common.enums;

/**
 * Created by liuhangjun on 2017/6/30.
 */
public enum CustomHttpStatus implements BaseEnum {

    DEFAULT_CUSTOM_STATUS(600, "default custom error code"),

    USER_NOT_LOGIN(601, "user not login"),
    
    REQUEST_DUPLICATE(602, "request duplicate");

    private Integer code;
    private String name;

    CustomHttpStatus(Integer code, String name){
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
