/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.entity.ShopVisit;
import com.channelsharing.hongqu.portal.api.service.ShopVisitService;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.dao.ShopVisitDao;

import java.util.List;

/**
 * 店铺访问记录Service
 * @author liuhangjun
 * @version 2018-07-08
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class ShopVisitServiceImpl extends CrudServiceImpl<ShopVisitDao, ShopVisit> implements ShopVisitService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    
    
    @Override
    public ShopVisit findOne(Long id) {
        ShopVisit entity = new ShopVisit();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "shopVisitList", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopVisitList:userId:' + #entity.userId")
    @Override
    public void add(ShopVisit entity){
        super.add(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "shopVisitList", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopVisitList:userId:' + #entity.userId")
    @Override
    public void delete(ShopVisit entity){
        super.delete(entity);
    }
    
    @Cacheable(value = PORTAL_CACHE_PREFIX + "shopVisitList", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopVisitList:userId:' +  #entity.userId", unless = "#result == null")
    @Override
    public List<ShopVisit> findList(ShopVisit entity){
    
        return super.dao.findList(entity);
    }


}
