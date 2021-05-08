/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.hongqu.portal.api.entity.ShopVisit;
import com.channelsharing.common.service.CrudService;

import java.util.List;


/**
 * 店铺访问记录Service
 * @author liuhangjun
 * @version 2018-07-08
 */
public interface ShopVisitService extends CrudService<ShopVisit>{
    
    List<ShopVisit> findList(ShopVisit entity);

}
