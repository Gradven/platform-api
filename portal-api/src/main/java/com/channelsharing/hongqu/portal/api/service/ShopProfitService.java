/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.hongqu.portal.api.entity.ShopProfit;
import com.channelsharing.common.service.CrudService;

import java.math.BigDecimal;


/**
 * 店铺收益Service
 * @author liuhangjun
 * @version 2018-07-17
 */
public interface ShopProfitService extends CrudService<ShopProfit>{
    
    BigDecimal unAvailableProfit(Long shopId);
    
    BigDecimal allProfit(Long shopId);

}
