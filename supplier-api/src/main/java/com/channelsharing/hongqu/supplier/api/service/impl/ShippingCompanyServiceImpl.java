/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.service.ShippingCompanyService;
import com.channelsharing.hongqu.supplier.api.entity.ShippingCompany;
import com.channelsharing.hongqu.supplier.api.dao.ShippingCompanyDao;

/**
 * 物流公司信息Service
 * @author liuhangjun
 * @version 2018-07-03
 */
@Service
public class ShippingCompanyServiceImpl extends CrudServiceImpl<ShippingCompanyDao, ShippingCompany> implements ShippingCompanyService {

    @Override
    public ShippingCompany findOne(Long id) {
        ShippingCompany entity = new ShippingCompany();
        entity.setId(id);

        return super.findOne(entity);
    }

}
