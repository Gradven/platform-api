/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.hongqu.supplier.api.constant.Constant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.service.GoodsParamService;
import com.channelsharing.hongqu.supplier.api.entity.GoodsParam;
import com.channelsharing.hongqu.supplier.api.dao.GoodsParamDao;

import java.util.List;

/**
 * 商品参数介绍Service
 * @author liuhangjun
 * @version 2018-07-29
 */
@Service
public class GoodsParamServiceImpl extends CrudServiceImpl<GoodsParamDao, GoodsParam> implements GoodsParamService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;  // 清除门户的缓存使用
    
    @Override
    public GoodsParam findOne(Long id) {
        GoodsParam entity = new GoodsParam();
        entity.setId(id);

        return super.findOne(entity);
    }
    
    
    @Override
    public List<GoodsParam> findList(Long goodsId){
        GoodsParam entity = new GoodsParam();
        entity.setGoodsId(goodsId);
        return super.dao.findList(entity);
    
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX
            + "goodsParamList", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsParamList:goodsId:' + #entity.goodsId")
    @Override
    public void add(GoodsParam entity){
       super.add(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX
            + "goodsParamList", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsParamList:goodsId:' + #entity.goodsId")
    @Override
    public void modify(GoodsParam entity){
        super.modify(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX
            + "goodsParamList", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsParamList:goodsId:' + #entity.goodsId")
    @Override
    public void delete(GoodsParam entity){
        super.delete(entity);
    }

}
