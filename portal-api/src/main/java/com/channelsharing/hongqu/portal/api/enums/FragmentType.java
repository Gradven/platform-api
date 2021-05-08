package com.channelsharing.hongqu.portal.api.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2017/6/19.
 */
public enum FragmentType implements BaseEnum {

     goods(1, "商品"), goodsCategory(2, "商品分类"), shop(3, "店铺");

    private Integer code;
    private String name;

    FragmentType(Integer code, String name){
        this.code = code;
        this.name = name;

    }

    

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
