/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.common.service.CrudService;
import com.channelsharing.hongqu.portal.api.entity.GoodsLike;


/**
 * 商品点赞Service
 * @author liuhangjun
 * @version 2018-07-27
 */
public interface GoodsLikeService extends CrudService<GoodsLike>{
    
    GoodsLike findOne(Long goodsId, Long userId);

}
