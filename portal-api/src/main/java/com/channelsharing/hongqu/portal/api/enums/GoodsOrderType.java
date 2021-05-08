package com.channelsharing.hongqu.portal.api.enums;

import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/6/26.
 */

public enum GoodsOrderType implements BaseEnum {
    
    last(1, "最新"), price(2, "价格"), category(3, "分类");
    
    private Integer code;
    private String name;
    
    GoodsOrderType(Integer code, String name){
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
