package com.channelsharing.hongqu.portal.api.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2017/6/19.
 */
public enum ProfitType implements BaseEnum {

     goodsProfit(1, "商品佣金收入"), regProfit(2, "拉新店主收入");

    private Integer code;
    private String name;

    ProfitType(Integer code, String name){
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
