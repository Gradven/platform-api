/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.hongqu.supplier.api.constant.Constant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.service.SupplierServiceService;
import com.channelsharing.hongqu.supplier.api.entity.SupplierService;
import com.channelsharing.hongqu.supplier.api.dao.SupplierServiceDao;

/**
 * 供应商售后服务Service
 * @author liuhangjun
 * @version 2018-08-08
 */
@Service
public class SupplierServiceServiceImpl extends CrudServiceImpl<SupplierServiceDao, SupplierService> implements SupplierServiceService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;  // 清除门户的缓存使用
    
    @Override
    public SupplierService findOne(Long id) {
        SupplierService entity = new SupplierService();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "supplierService", key = "#root.target.PORTAL_CACHE_PREFIX + 'supplierService:orderGoodsId:' + #entity.orderGoodsId")
    @Override
    public void modify(SupplierService entity){
        super.modify(entity);
    }

}
