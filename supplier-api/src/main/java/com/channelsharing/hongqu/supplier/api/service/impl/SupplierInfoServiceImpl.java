/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.dao.SupplierInfoDao;
import com.channelsharing.hongqu.supplier.api.entity.SupplierInfo;
import com.channelsharing.hongqu.supplier.api.service.SupplierInfoService;
import org.springframework.stereotype.Service;

/**
 * 供应商信息Service
 * @author liuhangjun
 * @version 2018-02-02
 */
@Service
public class SupplierInfoServiceImpl extends CrudServiceImpl<SupplierInfoDao, SupplierInfo> implements SupplierInfoService {
    
    @Override
    public SupplierInfo findOne(Long id) {
        SupplierInfo entity = new SupplierInfo();
        entity.setId(id);
        
        return super.findOne(entity);
    }
    
    
    
}
