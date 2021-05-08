/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.dao;

import com.channelsharing.common.dao.CrudDao;
import com.channelsharing.hongqu.portal.api.entity.ShopProfit;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * 店铺收益Dao接口
 * @author liuhangjun
 * @version 2018-07-17
 */
@Mapper
public interface ShopProfitDao extends CrudDao<ShopProfit> {
    
    BigDecimal unAvailableProfit(Long shopId);
    
    BigDecimal allProfit(Long shopId);
}
