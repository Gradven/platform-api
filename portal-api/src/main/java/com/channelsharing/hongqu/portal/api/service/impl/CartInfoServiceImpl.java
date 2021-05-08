/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.hongqu.portal.api.entity.*;
import com.channelsharing.hongqu.portal.api.service.*;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.hongqu.portal.api.constant.ConfigParamConstant;
import com.channelsharing.hongqu.portal.api.entity.*;
import com.channelsharing.hongqu.portal.api.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.dao.CartInfoDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车Service
 * @author liuhangjun
 * @version 2018-06-22
 */
@Service
public class CartInfoServiceImpl extends CrudServiceImpl<CartInfoDao, CartInfo> implements CartInfoService {

    @Autowired
    private GoodsInfoService goodsInfoService;
    
    @Autowired
    private ProductInfoService productInfoService;
    
    @Autowired
    private ShopGoodsService shopGoodsService;
    
    @Autowired
    private ShopInfoService shopInfoService;
    
    @Autowired
    private ConfigParamService configParamService;
    
    @Autowired
    private GoodsSpecificationService goodsSpecificationService;
    
    @Override
    public CartInfo findOne(Long id){
        CartInfo entity = new CartInfo();
        entity.setId(id);
        return super.findOne(entity);
    }
    
    /**
     * 将商品加入到购物车
     * 需要判断此商品店铺是否还在代理
     *
     * 对购物车对商品数量进行限制
     *
     * 判断是否还有库存
     *
     * @param entity
     */
    @Transactional
    @Override
    public void add(CartInfo entity){
        
        CartInfo cartInfo =  super.findOne(entity);
        if (cartInfo != null){
            throw new SystemInnerBusinessException("您已经添加了此商品到购物车了");
        }
    
        CartInfo cartInfoQuery = new CartInfo();
        cartInfoQuery.setUserId(entity.getUserId());
        
        Integer count = super.dao.findAllCount(cartInfoQuery);
        Integer maxNumber = NumberUtils.toInt(configParamService.findOne(ConfigParamConstant.PORTAL_CART_GOODS_MAX_NUMBER), 20);
        if (count > maxNumber){
            throw new SystemInnerBusinessException("购物车已满，请为购物车清理下空间吧");
        }
    
        // 如果店铺没有代理此商品或者代理的此商品已经被移除，那么提示用户不能购买
        ShopGoods shopGoods = shopGoodsService.findOne(entity.getShopId(), entity.getGoodsId());
        if (shopGoods == null || shopGoods.getStatus() == null || shopGoods.getStatus().equals(BooleanEnum.no.getCode())){
            throw new SystemInnerBusinessException("店铺已经不售卖此商品啦，换个宝贝看看");
        }
    
        GoodsInfo goodsInfo = goodsInfoService.findOne(entity.getGoodsId());
        if (goodsInfo != null){
            if (goodsInfo.getStoreNumber().equals(0)){
                throw new SystemInnerBusinessException("宝贝【"+goodsInfo.getName()+"】已经售罄啦，换个宝贝看看");
            }
        }
        
        super.add(entity);
    
    }
    
    /**
     * 购物车列表信息
     * 查出商铺信息（名称和logo）
     * 商品信息（名称和图片）
     * 产品信息（价格）
     * @param entity
     * @return
     */
    @Override
    public Paging<CartInfo> findPaging(CartInfo entity){
    
        Paging<CartInfo> cartInfoPaging = super.findPaging(entity);
        
        List<CartInfo> cartInoList = new ArrayList<>();
        
        for (CartInfo cartInfo : cartInfoPaging.getRows()){
    
            // 店铺信息
            ShopInfo shopInfo = shopInfoService.findOne(cartInfo.getShopId());
            if (shopInfo != null){
                shopInfo.setExpireTime(null);
                shopInfo.setPayFeeFlag(null);
                cartInfo.setShopInfo(shopInfo);
            }
            
            // 商品信息
            GoodsInfo goodsInfo = goodsInfoService.findOne(cartInfo.getGoodsId());
            if (goodsInfo != null){
                GoodsInfo goodsInfoNew = new GoodsInfo();
                goodsInfoNew.setName(goodsInfo.getName());
                goodsInfoNew.setPicUrls(goodsInfo.getPicUrls());
                goodsInfoNew.setInvoiceType(goodsInfo.getInvoiceType());
                goodsInfoNew.setService(goodsInfo.getService());
                goodsInfoNew.setCover(goodsInfo.getCover());
                cartInfo.setGoodsInfo(goodsInfoNew);
            }
            
            // 产品信息
            ProductInfo productInfo = productInfoService.findOne(cartInfo.getProductId());
            if (productInfo != null){
    
                ProductInfo productInfoNew = new ProductInfo();
                productInfoNew.setId(productInfo.getId());
                productInfoNew.setRetailPrice(productInfo.getRetailPrice());
                productInfoNew.setStoreNumber(productInfo.getStoreNumber());
                productInfoNew.setPicUrl(productInfo.getPicUrl());
    
                // 获取到规格的值组合
                String ids = productInfo.getGoodsSpecificationIds();
                String[] idArray = StringUtils.split(ids, ",");
                String[] specificationNameArray = new String[idArray.length];
                
                for(int i = 0; i < idArray.length; i ++){
                    GoodsSpecification goodsSpecification = goodsSpecificationService.findOne(Long.parseLong(idArray[i]));
                    if (goodsSpecification != null) {
                        specificationNameArray[i] = goodsSpecification.getValue();
                    }
                }
    
                productInfoNew.setGoodsSpecificationNames(specificationNameArray);
                
                cartInfo.setProductInfo(productInfoNew);
            }
            
    
            cartInoList.add(cartInfo);
        }
    
        cartInfoPaging.setRows(cartInoList);
        
        return cartInfoPaging;
    
    }
    
    @Transactional
    @Override
    public void batchDelete(Long[] ids, Long userId){
        if (ids != null && ids.length > 0){
            for (Long cartId : ids){
                CartInfo cartInfo = new CartInfo();
                cartInfo.setId(cartId);
                cartInfo.setUserId(userId);
                super.delete(cartInfo);
            }
        }
    }

}
