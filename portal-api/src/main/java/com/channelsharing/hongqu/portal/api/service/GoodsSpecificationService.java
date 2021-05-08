/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.GoodsSpecification;

import java.util.List;
import java.util.Map;


/**
 * 商品规格值Service
 * @author liuhangjun
 * @version 2018-06-20
 */
public interface GoodsSpecificationService extends CrudService<GoodsSpecification>{
    
    Map<String, List<GoodsSpecification>> combineGoodsSpecification(Long goodsId);

}
