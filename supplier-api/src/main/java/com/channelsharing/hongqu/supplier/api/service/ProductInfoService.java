/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service;

import com.channelsharing.hongqu.supplier.api.entity.ProductInfo;
import com.channelsharing.common.service.CrudService;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 产品信息Service
 * @author liuhangjun
 * @version 2018-06-07
 */
public interface ProductInfoService extends CrudService<ProductInfo>{
    
    List<ProductInfo> findList(Long goodsId);
}
