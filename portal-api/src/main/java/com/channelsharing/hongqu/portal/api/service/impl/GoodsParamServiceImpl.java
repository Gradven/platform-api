/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.GoodsParamService;
import com.channelsharing.hongqu.portal.api.entity.GoodsParam;
import com.channelsharing.hongqu.portal.api.dao.GoodsParamDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品参数介绍Service
 * @author liuhangjun
 * @version 2018-07-29
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class GoodsParamServiceImpl extends CrudServiceImpl<GoodsParamDao, GoodsParam> implements GoodsParamService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

    @Override
    public GoodsParam findOne(Long id) {
        GoodsParam entity = new GoodsParam();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @Cacheable(value = PORTAL_CACHE_PREFIX
            + "goodsParamList", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsParamList:goodsId:' + #goodsId")
    @Override
    public List<GoodsParam> findList(Long goodsId) {
        GoodsParam entity = new GoodsParam();
        entity.setGoodsId(goodsId);
        List<GoodsParam> goodsParamList =  super.dao.findList(entity);
        
        if (goodsParamList == null)
            goodsParamList = new ArrayList<>();
        
        return goodsParamList;
    }
}
