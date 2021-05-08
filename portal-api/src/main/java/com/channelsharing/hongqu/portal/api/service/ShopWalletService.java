/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.hongqu.portal.api.entity.ShopWallet;
import com.channelsharing.common.service.CrudService;


/**
 * 店铺钱包Service
 * @author liuhangjun
 * @version 2018-06-26
 */
public interface ShopWalletService extends CrudService<ShopWallet>{
    
    ShopWallet findOneByShopId(Long shopId);

}
