/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.AddressInfo;


/**
 * 用户地址信息Service
 * @author liuhangjun
 * @version 2018-07-16
 */
public interface AddressInfoService extends CrudService<AddressInfo>{
    
    AddressInfo findDefaultAddress(Long userId);

}
