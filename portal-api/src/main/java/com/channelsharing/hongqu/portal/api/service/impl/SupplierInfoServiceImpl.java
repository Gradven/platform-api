/**
 * Copyright &copy; 2016-2022 供应商信息 All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.SupplierInfoService;
import com.channelsharing.hongqu.portal.api.entity.SupplierInfo;
import com.channelsharing.hongqu.portal.api.dao.SupplierInfoDao;

/**
 * 供应商信息Service
 * @author 供应商信息
 * @version 2018-08-08
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