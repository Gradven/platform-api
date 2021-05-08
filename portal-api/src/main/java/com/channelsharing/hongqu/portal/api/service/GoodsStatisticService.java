/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.GoodsStatistic;


/**
 * 商品实时数据统计Service
 * @author liuhangjun
 * @version 2018-07-27
 */
public interface GoodsStatisticService extends CrudService<GoodsStatistic>{
    
    GoodsStatistic findOneByGoodsId(Long goodsId);

}
