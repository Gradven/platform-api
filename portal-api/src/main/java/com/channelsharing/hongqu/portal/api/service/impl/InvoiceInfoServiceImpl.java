/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.service.InvoiceInfoService;
import com.channelsharing.hongqu.portal.api.entity.InvoiceInfo;
import com.channelsharing.hongqu.portal.api.dao.InvoiceInfoDao;

import javax.validation.constraints.NotNull;

/**
 * 发票信息Service
 * @author liuhangjun
 * @version 2018-07-29
 */
@Service
public class InvoiceInfoServiceImpl extends CrudServiceImpl<InvoiceInfoDao, InvoiceInfo> implements InvoiceInfoService {

    @Override
    public InvoiceInfo findOne(Long id) {
        InvoiceInfo entity = new InvoiceInfo();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    @Override
    public InvoiceInfo findOne(Long id, Long userId) {
        InvoiceInfo entity = new InvoiceInfo();
        entity.setId(id);
        entity.setUserId(userId);
        
        return super.findOne(entity);
    }
    
    @Transactional
    @Override
    public void add(InvoiceInfo entity){
    
        this.setOtherNotDefault(entity);
        super.add(entity);
    }
    
    @Transactional
    @Override
    public void modify(@NotNull InvoiceInfo entity){
        
        if (entity.getId() == null && entity.getUserId() == null){
            throw new SystemInnerBusinessException("发票的id主键和用户id不能为空");
            
        }
        
        this.setOtherNotDefault(entity);
        
        super.modify(entity);
    }
    
    
    /**
     * 如果是增加默认的发票，则把之前的对应的type默认发票修改为不默认
     * @param entity
     */
    private void setOtherNotDefault(InvoiceInfo entity){
        
        if (entity.getUserId() == null || entity.getDefaultFlag() == null){
            return;
        }
        
        if (entity.getDefaultFlag().equals(BooleanEnum.yes.getCode())){
            InvoiceInfo query = new InvoiceInfo();
            query.setUserId(entity.getUserId());
            query.setDefaultFlag(BooleanEnum.yes.getCode());
            InvoiceInfo result = super.findOne(query);
            
            if (result != null){
                InvoiceInfo invoiceInfoUpdate = new InvoiceInfo();
                invoiceInfoUpdate.setUserId(entity.getUserId());
                invoiceInfoUpdate.setDefaultFlag(BooleanEnum.no.getCode());
                invoiceInfoUpdate.setType(entity.getType());
                this.modify(invoiceInfoUpdate);
            }
            
        }
    }
    


}
