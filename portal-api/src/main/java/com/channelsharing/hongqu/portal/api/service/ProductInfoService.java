/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.ProductInfo;

import java.util.List;


/**
 * 商品的产品信息Service
 * @author liuhangjun
 * @version 2018-06-20
 */
public interface ProductInfoService extends CrudService<ProductInfo>{
    
    void addSalesVolume(Integer addNum, Long id, Long goodsId);
    
    void addStoreNumber(Integer addNum, Long id, Long goodsId);
    
    List<ProductInfo> findList(Long goodsId);
    
    Integer sumGoodsStore(Long goodsId);

}
