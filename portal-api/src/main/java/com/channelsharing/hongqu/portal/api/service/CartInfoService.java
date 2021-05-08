/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.CartInfo;


/**
 * 购物车Service
 * @author liuhangjun
 * @version 2018-06-22
 */
public interface CartInfoService extends CrudService<CartInfo>{

    void batchDelete(Long[] ids, Long userId);

}
