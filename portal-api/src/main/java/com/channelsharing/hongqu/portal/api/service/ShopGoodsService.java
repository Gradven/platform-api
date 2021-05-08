/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.entity.Paging;
import com.channelsharing.hongqu.portal.api.entity.ShopGoods;
import com.channelsharing.common.service.CrudService;

import javax.validation.constraints.NotNull;


/**
 * 店铺代理商品信息Service
 * @author liuhangjun
 * @version 2018-06-12
 */
public interface ShopGoodsService extends CrudService<ShopGoods>{

    ShopGoods findOne(@NotNull Long shopId, @NotNull Long goodsId);
    
    Paging<ShopGoods> findPaging(ShopGoods entity, Long userId);

}
