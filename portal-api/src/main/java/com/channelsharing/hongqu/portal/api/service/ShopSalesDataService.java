/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.ShopSalesData;

public interface ShopSalesDataService extends CrudService<ShopSalesData> {
	ShopSalesData findByShopId(Long shopId);
}
