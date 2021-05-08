/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.GoodsStatistic;
import com.channelsharing.hongqu.portal.api.service.GoodsStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.GoodsLikeService;
import com.channelsharing.hongqu.portal.api.entity.GoodsLike;
import com.channelsharing.hongqu.portal.api.dao.GoodsLikeDao;

/**
 * 商品点赞Service
 * @author liuhangjun
 * @version 2018-07-27
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class GoodsLikeServiceImpl extends CrudServiceImpl<GoodsLikeDao, GoodsLike> implements GoodsLikeService {
    
    @Autowired
    private GoodsStatisticService goodsStatisticService;
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

    @Override
    public GoodsLike findOne(Long id) {
        GoodsLike entity = new GoodsLike();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @Cacheable(value = PORTAL_CACHE_PREFIX
            + "goodsLike", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsLike:userId_goodsId:' + #userId + '_' + #goodsId")
    @Override
    public GoodsLike findOne(Long goodsId, Long userId){
        GoodsLike entity = new GoodsLike();
        entity.setUserId(userId);
        entity.setGoodsId(goodsId);
        GoodsLike goodsLike = super.findOne(entity);
        
        if (goodsLike == null)
            goodsLike = new GoodsLike();
        
        return goodsLike;
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX
            + "goodsLike", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsLike:userId_goodsId:' + #entity.userId + '_' + #entity.goodsId")
    @Override
    public void add(GoodsLike entity) {
        super.add(entity);
    
        // 增加统计点赞数
        GoodsStatistic goodsStatistic = new GoodsStatistic();
        goodsStatistic.setGoodsId(entity.getGoodsId());
        goodsStatistic.setLikeCount(1);
        goodsStatisticService.add(goodsStatistic);
    }
}
