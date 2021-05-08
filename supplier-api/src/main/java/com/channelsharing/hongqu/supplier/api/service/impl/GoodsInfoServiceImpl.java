/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.service.impl;

import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.hongqu.supplier.api.service.GoodsDescriptionService;
import com.channelsharing.hongqu.supplier.api.service.GoodsInfoService;
import com.channelsharing.common.cache.CacheDuration;
import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.utils.DateUtils;
import com.channelsharing.common.utils.RandomUtil;
import com.channelsharing.hongqu.supplier.api.constant.Constant;
import com.channelsharing.hongqu.supplier.api.entity.GoodsDescription;
import com.channelsharing.hongqu.supplier.api.entity.ProductInfo;
import com.channelsharing.hongqu.supplier.api.service.ProductInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.supplier.api.entity.GoodsInfo;
import com.channelsharing.hongqu.supplier.api.dao.GoodsInfoDao;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

/**
 * 商品信息接口Service
 * @author liuhangjun
 * @version 2018-06-06
 */
@CacheDuration(duration = ExpireTimeConstant.ONE_DAY)
@Service
public class GoodsInfoServiceImpl extends CrudServiceImpl<GoodsInfoDao, GoodsInfo> implements GoodsInfoService {
    
    @Autowired
    private GoodsDescriptionService goodsDescriptionService;
    
    @Autowired
    private ProductInfoService productInfoService;
    
    public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;  // 清除门户的缓存使用
    
    /**
     * @param id
     * @return
     */
    @Override
    public GoodsInfo findOne(Long id){
        GoodsInfo entity = new GoodsInfo();
        entity.setId(id);
        GoodsInfo goodsInfo = super.findOne(entity);
        return goodsInfo;
    }
    
    /**
     * 查询单个商品时，加上详情内容
     * @param entity
     * @return
     */
    @Override
    public GoodsInfo findOne(@NotNull GoodsInfo entity){

        GoodsInfo goodsInfo = super.findOne(entity);
        if (goodsInfo == null){
            return new GoodsInfo();
        }
        
        GoodsDescription goodsDescription = new GoodsDescription();
        goodsDescription.setId(entity.getId());
        GoodsDescription goodsDescriptionResult = goodsDescriptionService.findOne(goodsDescription);
        
        String description;
        if (null != goodsDescriptionResult){
            description = goodsDescriptionResult.getContent();
            goodsInfo.setDescription(description);
        }
        
        return goodsInfo;
    }
    
    
    /**
     * 增加商品信息和简介
     * @param entity
     */
    @Transactional
    @Override
    public GoodsInfo addWithResult(GoodsInfo entity){
        entity.setSn(this.generateGoodsSn(entity.getSupplierId(), entity.getCreateSuId()));
        GoodsInfo goodsInfo = super.addWithResult(entity);
    
        GoodsDescription goodsDescription = new GoodsDescription();
        goodsDescription.setContent(entity.getDescription());
        goodsDescription.setId(goodsInfo.getId());
        goodsDescriptionService.add(goodsDescription);
    
        goodsInfo.setDescription(entity.getDescription());
    
        this.goodsModifyOnSale(goodsInfo);
        
        return goodsInfo;
        
    }
    
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "goodsInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsInfo:id:' + #entity.id")
    @Transactional
    @Override
    public void modify(GoodsInfo entity){
    
        this.goodsModifyOnSale(entity);
        
        super.modify(entity);
    
        if (StringUtils.isNotBlank(entity.getDescription())){
            GoodsDescription goodsDescription = new GoodsDescription();
            goodsDescription.setId(entity.getId());
            goodsDescription.setContent(entity.getDescription());
            goodsDescriptionService.modify(goodsDescription);
        }
        
    
        
        
    }
    
    /**
     * 功能：修改商品上下架状态业务处理
     * 如果商品要上架，判断是否有产品，没有的话需要新增产品
     * @param entity
     */
    private void goodsModifyOnSale(GoodsInfo entity){
        if (entity.getOnSaleFlag() != null ){
    
            GoodsInfo goodsInfoRet = this.findOne(entity.getId());
            String goodsName = goodsInfoRet.getName();
            
            if (entity.getOnSaleFlag().equals(BooleanEnum.yes.getCode())){
    
                // 上架时提示有没有产品，如果没有产品信息，不能上架
                ProductInfo productInfo = new ProductInfo();
                productInfo.setGoodsId(entity.getId());
                ProductInfo productInfoResult = productInfoService.findOne(productInfo);
    
    
                if (productInfoResult == null){
                    throw new SystemInnerBusinessException("上架【" + goodsName + "】商品之前，请给商品填写规格和定价");
                }
            }

        }
    }
    
    @CacheEvict(value = PORTAL_CACHE_PREFIX + "goodsInfo", key = "#root.target.PORTAL_CACHE_PREFIX + 'goodsInfo:id:' + #entity.id")
    @Transactional
    @Override
    public void delete(GoodsInfo entity){
        super.delete(entity);
    
        GoodsDescription goodsDescription = new GoodsDescription();
        goodsDescription.setId(entity.getId());
        goodsDescription.setContent(entity.getDescription());
        goodsDescriptionService.delete(goodsDescription);
    }
    
    
    
    /**
     * 商品编号
     * @return
     */
    private String generateGoodsSn(Long supplierId, Long createSuId){
        
        StringBuffer sn = new StringBuffer();
        sn.append(Long.toString(supplierId));
        sn.append(Long.toString(createSuId));
        sn.append(DateUtils.getShortTime().toString());
        sn.append(RandomUtil.getRandomNumString(5));
        
        return sn.toString();
    }
    

}
