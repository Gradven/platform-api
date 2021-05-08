/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.hongqu.supplier.api.dao.ProductInfoDao;
import com.channelsharing.hongqu.supplier.api.entity.GoodsInfo;
import com.channelsharing.hongqu.supplier.api.entity.ProductInfo;
import com.channelsharing.hongqu.supplier.api.service.GoodsInfoService;
import com.channelsharing.hongqu.supplier.api.service.ProductInfoService;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.pub.enums.ApproveStatus;
import com.channelsharing.hongqu.supplier.api.constant.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 产品信息Service
 * @author liuhangjun
 * @version 2018-06-07
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class ProductInfoServiceImpl extends CrudServiceImpl<ProductInfoDao, ProductInfo> implements ProductInfoService {
    
    
    @Autowired
    private GoodsInfoService goodsInfoService;
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;
    
    /**
     * 1、增加产品时，重新计算商品的库存，所以需要把商品的信息缓存清除
     * 2、增加产品时，需要计算出一个不重复的产品序列号sn
     * @param entity
     */
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "goodsInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsInfo:id:' + #entity.goodsId")
    @Transactional
    @Override
    public void add(ProductInfo entity){
        
        super.add(entity);
    
        this.modifyGoodsInfo(entity.getGoodsId());
       
    }
    
    /**
     * 1、增加产品时，重新计算商品的库存，所以需要把商品的信息缓存清除
     * 2、增加产品时，需要计算出一个不重复的产品序列号sn
     * @param entity
     */
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "goodsInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsInfo:id:' + #entity.goodsId")
    @Transactional
    @Override
    public ProductInfo addWithResult(ProductInfo entity){
        
        super.add(entity);
        
        this.modifyGoodsInfo(entity.getGoodsId());
    
        return super.findOne(entity);
        
    }
    
    @Override
    public ProductInfo findOne(Long id) {
        ProductInfo entity = new ProductInfo();
        entity.setId(id);
        
        return super.findOne(entity);
    }
    
    @Override
    public List<ProductInfo> findList(Long goodsId){
        ProductInfo entity = new ProductInfo();
        entity.setGoodsId(goodsId);
        entity.setLimit(Constant.MAX_LIMIT);
        return super.dao.findList(entity);
    }
    
    
    
    /**
     * 修改产品时，重新计算商品的库存
     * 所以需要把商品的信息缓存清除
     * @param entity
     */
    @Caching(evict = {@CacheEvict(value = PORTAL_CACHE_PREFIX + "productInfoList", key = "#root.target.PORTAL_CACHE_PREFIX + 'productInfoList:goodsId:' + #entity.goodsId"),
                      @CacheEvict(value = PORTAL_CACHE_PREFIX + "goodsInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsInfo:id:' + #entity.goodsId")})
    @Transactional
    @Override
    public void modify(@NotNull ProductInfo entity){
        
        if ((entity.getGoodsId() == null)
                && (entity.getId() == null)
                && StringUtils.isBlank(entity.getSn())
                && StringUtils.isBlank(entity.getGoodsSn())){
        
            throw new BadRequestException("请求修改产品时，商品id、产品id、商品sn和产品sn不能同时为空");
        }
        
        super.modify(entity);
    
        // 修改产品时，重新计算商品的库存
        this.modifyGoodsInfo(entity.getGoodsId());
        
        
    }
    
    /**
     * 计算商品信息,
     * 库存：所有产品的库存进行相加处理
     * 利润：取利润的最高价格
     * 零售价：取最高利润对应的零售价
     * 出厂价： 取最高利润对应的出厂价
     * @param goodsId
     */
    private void modifyGoodsInfo(Long goodsId){
    
        ProductInfo query = new ProductInfo();
        query.setGoodsId(goodsId);
        query.setLimit(Constant.MAX_LIMIT);
    
        int totalStoreNumber = 0;
        BigDecimal highestProfit = null;
        BigDecimal retailPrice = null;
        BigDecimal unitPrice = null;
        
        List<ProductInfo> productInfoList = super.findPaging(query).getRows();
        for (ProductInfo productInfo : productInfoList){
            // 库存相加
            totalStoreNumber = totalStoreNumber + productInfo.getStoreNumber();
            
            // 利润比较, 选择最大的值
            BigDecimal profit = productInfo.getProfit();
            if (highestProfit == null){
                highestProfit = productInfo.getProfit();
                retailPrice = productInfo.getRetailPrice();
                unitPrice = productInfo.getUnitPrice();
            }else {
                int result = profit.compareTo(highestProfit);
                if (result > 0){
                    highestProfit = profit;
                    retailPrice = productInfo.getRetailPrice();
                    unitPrice = productInfo.getUnitPrice();
                    
                }
            }
            
        }
    
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setId(goodsId);
        goodsInfo.setStoreNumber(totalStoreNumber);
        goodsInfo.setProfit(highestProfit);
        goodsInfo.setRetailPrice(retailPrice);
        goodsInfo.setUnitPrice(unitPrice);
        goodsInfoService.modify(goodsInfo);
    }
    

}
