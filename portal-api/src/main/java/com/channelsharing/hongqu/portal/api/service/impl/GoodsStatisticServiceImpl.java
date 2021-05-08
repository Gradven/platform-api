/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.GoodsLike;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.GoodsStatisticService;
import com.channelsharing.hongqu.portal.api.entity.GoodsStatistic;
import com.channelsharing.hongqu.portal.api.dao.GoodsStatisticDao;

/**
 * 商品实时数据统计Service
 * @author liuhangjun
 * @version 2018-07-27
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class GoodsStatisticServiceImpl extends CrudServiceImpl<GoodsStatisticDao, GoodsStatistic> implements GoodsStatisticService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

    @Override
    public GoodsStatistic findOne(Long id) {
        GoodsStatistic entity = new GoodsStatistic();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    
    @Cacheable(value = PORTAL_CACHE_PREFIX
            + "goodsStatistic", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsStatistic:goodsId:' + #goodsId")
    @Override
    public GoodsStatistic findOneByGoodsId(Long goodsId){
        
        GoodsStatistic entity = new GoodsStatistic();
        entity.setGoodsId(goodsId);
    
        GoodsStatistic goodsStatistic = super.findOne(entity);
        
        if (goodsStatistic == null)
            goodsStatistic = new GoodsStatistic();
        
        return goodsStatistic;
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX
            + "goodsStatistic", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsStatistic:goodsId:' + #entity.goodsId")
    @Override
    public void add(GoodsStatistic entity){
        super.add(entity);
    }


}
