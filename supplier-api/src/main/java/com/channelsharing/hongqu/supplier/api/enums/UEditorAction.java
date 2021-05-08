package com.channelsharing.hongqu.supplier.api.enums;

import com.channelsharing.common.enums.BaseEnum;

/**
 * 百度uEditor请求校验枚举值
 * Created by liuhangjun on 2018/4/10.
 */
public enum UEditorAction implements BaseEnum {

    config(0, "config"), uploadimage(1, "uploadimage"), uploadscrawl(2, "uploadscrawl"), uploadvideo(3, "uploadvideo")
            , uploadfile(2, "uploadfile"), catchimage(2, "catchimage");


    private Integer code;
    private String name;

    UEditorAction(Integer code, String name){
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
