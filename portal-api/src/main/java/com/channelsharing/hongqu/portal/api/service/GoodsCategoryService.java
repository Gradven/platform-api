/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.GoodsCategory;

/**
 * 商品分类Service
 * 
 * @author liuhangjun
 * @version 2018-06-27
 */
public interface GoodsCategoryService extends CrudService<GoodsCategory> {
	GoodsCategory getAllCategories();
}
