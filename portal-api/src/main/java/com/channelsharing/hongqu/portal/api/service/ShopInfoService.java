/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.hongqu.portal.api.entity.ShopInfo;
import com.channelsharing.common.service.CrudService;


/**
 * 店铺信息Service
 * @author liuhangjun
 * @version 2018-06-11
 */
public interface ShopInfoService extends CrudService<ShopInfo>{
    
    ShopInfo findByName(String name);

}
