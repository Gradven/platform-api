package com.channelsharing.pub.constant;


/**
 * Created by liuhangjun on 2018/2/9.
 */
public interface Constant {

    String HTTP = "http";
    String HTTP_PREFIX = "http://";


    // 定义一个 从数据库获取limit的数量 常量值
    int MAX_LIMIT = 999;
    
    
    // 各个模块redis缓存前缀
    String PORTAL_CACHE_PREFIX = "portal:";
    String SUPPLIER_CACHE_PREFIX = "supplier:";

}
