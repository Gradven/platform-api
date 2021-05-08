/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.GoodsDescriptionService;
import com.channelsharing.hongqu.portal.api.entity.GoodsDescription;
import com.channelsharing.hongqu.portal.api.dao.GoodsDescriptionDao;

/**
 * 商品介绍内容Service
 * @author liuhangjun
 * @version 2018-06-12
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class GoodsDescriptionServiceImpl extends CrudServiceImpl<GoodsDescriptionDao, GoodsDescription> implements GoodsDescriptionService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    
    @Cacheable(value = PORTAL_CACHE_PREFIX + "goodsDescription", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsDescription:id:' + #id", unless = "#result == null")
    @Override
    public GoodsDescription findOne(Long id) {
        GoodsDescription entity = new GoodsDescription();
        entity.setId(id);
        GoodsDescription goodsDescription = super.findOne(entity);
        
        return goodsDescription;
    }

}
