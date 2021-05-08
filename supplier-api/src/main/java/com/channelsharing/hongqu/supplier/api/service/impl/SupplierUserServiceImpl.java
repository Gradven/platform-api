/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.hongqu.supplier.api.dao.SupplierUserDao;
import com.channelsharing.hongqu.supplier.api.enums.SupplierUserStatus;
import com.channelsharing.hongqu.supplier.api.service.SupplierUserService;
import com.channelsharing.hongqu.supplier.api.entity.SupplierUser;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.service.CrudServiceImpl;
import lombok.NonNull;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 供应商用户Service
 * @author liuhangjun
 * @version 2018-02-02
 */
@Service
public class SupplierUserServiceImpl extends CrudServiceImpl<SupplierUserDao, SupplierUser> implements SupplierUserService {
    
    @Override
    public SupplierUser findOne(Long id) {
        SupplierUser entity = new SupplierUser();
        entity.setId(id);
        
        return super.findOne(entity);
    }
    
    @Override
    public SupplierUser login(@NonNull SupplierUser supplierUser) {
        SupplierUser queryEntity = new SupplierUser();
        queryEntity.setAccount(supplierUser.getAccount());
        SupplierUser result = findOne(queryEntity);
    
        if (result == null)
            throw new BadRequestException("用户名或密码错误");
    
        if (result.getStatus() == SupplierUserStatus.forbidden.getCode())
            throw new BadRequestException("用户被禁止登录");
    
        //比较密码是否可以对应上
        String password = result.getPassword();
        String reqPassword = DigestUtils.sha512Hex(supplierUser.getPassword());
    
        logger.debug("Request password is: [{}]; digest password is: [{}] ", result.getPassword(), reqPassword);
    
        if (!StringUtils.equals(reqPassword, password)) {
            throw new BadRequestException("用户名或密码错误");
        }
    
        return result;
    }
    
    @Override
    public void modifyPassword(Long supplierUserId, String oldPwd, String newPwd) {
    
        SupplierUser supplierUser = new SupplierUser();
        supplierUser.setId(supplierUserId);
    
        SupplierUser retEntUser = super.findOne(supplierUser);
    
        if (!DigestUtils.sha512Hex(oldPwd).equals(retEntUser.getPassword())) {
            throw new BadRequestException("原始密码错误");
        }else {
            BeanUtils.copyProperties(retEntUser, supplierUser);
        
            supplierUser.setPassword(DigestUtils.sha512Hex(newPwd));
            super.modify(supplierUser);
            
        }
    
    }
}
