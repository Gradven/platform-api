/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.SupplierService;


/**
 * 供应商售后服务Service
 * @author liuhangjun
 * @version 2018-08-08
 */
public interface SupplierServiceService extends CrudService<SupplierService>{
    
    SupplierService findOneByOrderGoodsId(Long orderGoodsId);

}
