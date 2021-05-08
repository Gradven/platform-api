/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.dao.ShopProfitDao;
import com.channelsharing.hongqu.portal.api.entity.ShopProfit;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.ShopProfitService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺收益Service
 * @author liuhangjun
 * @version 2018-07-17
 */
@Service
public class ShopProfitServiceImpl extends CrudServiceImpl<ShopProfitDao, ShopProfit> implements ShopProfitService {

    @Override
    public ShopProfit findOne(Long id) {
        ShopProfit entity = new ShopProfit();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    /**
     * mysql sum计算待入账收益
     * @param shopId
     * @return
     */
    @Override
    public BigDecimal unAvailableProfit(Long shopId){
    
        BigDecimal unAvailableProfit = super.dao.unAvailableProfit(shopId);
        
        return unAvailableProfit;
    }
    
    /**
     * mysql sum计算待入账收益
     * @param shopId
     * @return
     */
    @Override
    public BigDecimal allProfit(Long shopId){
        
        BigDecimal allProfit = super.dao.allProfit(shopId);
        
        return allProfit;
    }
    


}
