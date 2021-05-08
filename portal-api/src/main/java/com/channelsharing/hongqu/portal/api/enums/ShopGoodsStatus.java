package com.channelsharing.hongqu.portal.api.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * 店主商品代理状态
 * Created by liuhangjun on 2018/1/24.
 */
public enum ShopGoodsStatus implements BaseEnum {
    
    add(1, "添加到代理"), move(-1, "移除代理");

    private Integer code;
    private String name;

    ShopGoodsStatus(Integer code, String name){
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
