/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.InvoiceInfo;


/**
 * 发票信息Service
 * @author liuhangjun
 * @version 2018-07-29
 */
public interface InvoiceInfoService extends CrudService<InvoiceInfo>{
    
    InvoiceInfo findOne(Long id, Long userId);

}
