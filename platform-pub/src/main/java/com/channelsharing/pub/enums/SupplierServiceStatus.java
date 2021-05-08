package com.channelsharing.pub.enums;

import com.channelsharing.common.enums.BaseEnum;

/**
 * Created by liuhangjun on 2018/8/8.
 */
public enum SupplierServiceStatus  implements BaseEnum {
    
    apply(1, "申请"), approved(2, "审核通过"), unapproved(3, "审核不通过"), cancel(99, "取消退换货");
    
    private Integer code;
    private String name;
    
    SupplierServiceStatus(Integer code, String name){
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
