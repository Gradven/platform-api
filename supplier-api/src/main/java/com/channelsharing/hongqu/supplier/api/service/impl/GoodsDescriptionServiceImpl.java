/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.hongqu.supplier.api.constant.Constant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.service.GoodsDescriptionService;
import com.channelsharing.hongqu.supplier.api.entity.GoodsDescription;
import com.channelsharing.hongqu.supplier.api.dao.GoodsDescriptionDao;

/**
 * 商品介绍内容Service
 * @author liuhangjun
 * @version 2018-06-07
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class GoodsDescriptionServiceImpl extends CrudServiceImpl<GoodsDescriptionDao, GoodsDescription> implements GoodsDescriptionService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    
    @Override
    public GoodsDescription findOne(Long id) {
        GoodsDescription entity = new GoodsDescription();
        entity.setId(id);
        
        return super.findOne(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "goodsDescription", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsDescription:id:' + #entity.id")
    @Override
    public void modify(GoodsDescription entity) {
        super.modify(entity);
    }

}
