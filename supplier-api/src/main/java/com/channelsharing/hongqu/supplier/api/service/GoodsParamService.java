/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.supplier.api.entity.GoodsParam;

import java.util.List;


/**
 * 商品参数介绍Service
 * @author liuhangjun
 * @version 2018-07-29
 */
public interface GoodsParamService extends CrudService<GoodsParam>{
    
    List<GoodsParam> findList(Long goodsId);
}
