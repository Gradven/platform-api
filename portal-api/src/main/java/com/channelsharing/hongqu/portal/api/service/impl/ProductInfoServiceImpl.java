/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.hongqu.portal.api.constant.Constant;
import com.channelsharing.hongqu.portal.api.dao.ProductInfoDao;
import com.channelsharing.hongqu.portal.api.service.ProductInfoService;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.entity.ProductInfo;

import java.util.List;

/**
 * 商品的产品信息Service
 * @author liuhangjun
 * @version 2018-06-20
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class ProductInfoServiceImpl extends CrudServiceImpl<ProductInfoDao, ProductInfo> implements ProductInfoService {
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    
    @Override
    public ProductInfo findOne(Long id) {
        ProductInfo entity = new ProductInfo();
        entity.setId(id);
        
        return super.findOne(entity);
    }
    
    
    @Cacheable(value = PORTAL_CACHE_PREFIX + "productInfoList", key = "#root.target.PORTAL_CACHE_PREFIX + 'productInfoList:goodsId:' + #goodsId")
    @Override
    public List<ProductInfo> findList(Long goodsId){
        ProductInfo entity = new ProductInfo();
        entity.setGoodsId(goodsId);
        entity.setDelFlag(BooleanEnum.no.getCode());
        entity.setLimit(Constant.MAX_LIMIT);
        return super.dao.findList(entity);
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "productInfoList", key = "#root.target.PORTAL_CACHE_PREFIX + 'productInfoList:goodsId:' + #entity.goodsId")
    @Override
    public void modify(ProductInfo entity){
        super.modify(entity);
    }
    
    /**
     * 增加销量
     * @param addNum
     * @param id
     * @param goodsId 只用来清除缓存使用
     */
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "productInfoList", key = "#root.target.PORTAL_CACHE_PREFIX + 'productInfoList:goodsId:' + #goodsId")
    @Override
    public void addSalesVolume(Integer addNum, Long id, Long goodsId){
        super.dao.addSalesVolume(addNum, id);
    }
    
    /**
     * 增加库存
     * @param addNum
     * @param id
     * @param goodsId 只是用来清缓存使用
     */
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "productInfoList", key = "#root.target.PORTAL_CACHE_PREFIX + 'productInfoList:goodsId:' + #goodsId")
    @Override
    public void addStoreNumber(Integer addNum, Long id, Long goodsId) {
        super.dao.addStoreNumber(addNum, id);
    }
    
    
    /**
     * 统计商品的库存数
     * @param goodsId
     * @return
     */
    public Integer sumGoodsStore(Long goodsId){
        return super.dao.sumGoodsStore(goodsId);
    }
    
}
