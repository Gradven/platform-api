package com.channelsharing.hongqu.portal.api.enums;


import com.channelsharing.common.enums.BaseEnum;

/**
 * 店主商品代理状态
 * Created by liuhangjun on 2018/1/24.
 */
public enum AgentFlag implements BaseEnum {
    
    onceAgent(-1, "曾经代理"), unAgent(0, "未代理"), agent(1, "代理中");

    private Integer code;
    private String name;

    AgentFlag(Integer code, String name){
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
