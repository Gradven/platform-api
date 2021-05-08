/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.SupplierServiceService;
import com.channelsharing.hongqu.portal.api.entity.SupplierService;
import com.channelsharing.hongqu.portal.api.dao.SupplierServiceDao;

/**
 * 供应商售后服务Service
 * @author liuhangjun
 * @version 2018-08-08
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class SupplierServiceServiceImpl extends CrudServiceImpl<SupplierServiceDao, SupplierService> implements SupplierServiceService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

    @Override
    public SupplierService findOne(Long id) {
        SupplierService entity = new SupplierService();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @Cacheable(value = PORTAL_CACHE_PREFIX
            + "supplierService", key = "#root.target.PORTAL_CACHE_PREFIX + 'supplierService:orderGoodsId:' + #orderGoodsId")
    @Override
    public SupplierService findOneByOrderGoodsId(Long orderGoodsId){
        SupplierService entity = new SupplierService();
        entity.setOrderGoodsId(orderGoodsId);
    
        return super.findOne(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX
            + "supplierService", key = "#root.target.PORTAL_CACHE_PREFIX + 'supplierService:orderGoodsId:' + #entity.orderGoodsId")
    @Override
    public void modify(SupplierService entity){
        super.modify(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX
            + "supplierService", key = "#root.target.PORTAL_CACHE_PREFIX + 'supplierService:orderGoodsId:' + #entity.orderGoodsId")
    @Override
    public void delete(SupplierService entity){
        super.delete(entity);
    }
    
    
    
}
