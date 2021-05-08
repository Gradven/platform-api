package com.channelsharing.hongqu.supplier.api.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/1/24.
 */
public enum GoodsOnSaleFlag implements BaseEnum {

    notSale(0, "下架"), onSale(1, "上架"),;

    private Integer code;
    private String name;

    GoodsOnSaleFlag(Integer code, String name){
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
