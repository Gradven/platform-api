/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service;

import com.channelsharing.hongqu.supplier.api.entity.SupplierUser;
import com.channelsharing.common.service.CrudService;


/**
 * 供应商用户Service
 * @author liuhangjun
 * @version 2018-02-02
 */
public interface SupplierUserService extends CrudService<SupplierUser> {

    SupplierUser login(SupplierUser supplierUser);

    void modifyPassword(Long supplierUserId, String oldPwd, String newPwd);

}
