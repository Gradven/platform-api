/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.entity.SpecificationInfo;
import com.channelsharing.hongqu.portal.api.service.SpecificationInfoService;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.dao.SpecificationInfoDao;

/**
 * 规格维度接口Service
 * @author liuhangjun
 * @version 2018-06-20
 */
@Service
public class SpecificationInfoServiceImpl extends CrudServiceImpl<SpecificationInfoDao, SpecificationInfo> implements SpecificationInfoService {
    
    public SpecificationInfo findOne(Long id) {
        SpecificationInfo entity = new SpecificationInfo();
        entity.setId(id);
        
        return super.findOne(entity);
    }


}
