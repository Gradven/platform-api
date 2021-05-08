/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.hongqu.supplier.api.dao.SpecificationInfoDao;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.service.SpecificationInfoService;
import com.channelsharing.hongqu.supplier.api.entity.SpecificationInfo;

/**
 * 规格维度接口Service
 * @author liuhangjun
 * @version 2018-06-07
 */
@Service
public class SpecificationInfoServiceImpl extends CrudServiceImpl<SpecificationInfoDao, SpecificationInfo> implements SpecificationInfoService {
    
    @Override
    public SpecificationInfo findOne(Long id) {
        SpecificationInfo entity = new SpecificationInfo();
        entity.setId(id);
        
        return super.findOne(entity);
    }

}
