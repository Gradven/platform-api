package com.channelsharing.hongqu.portal.api.service.impl;

import com.channelsharing.common.cache.ExpireTimeConstant;
import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.utils.DateUtils;
import com.channelsharing.hongqu.portal.api.constant.ConfigParamConstant;
import com.channelsharing.hongqu.portal.api.entity.ShopProfit;
import com.channelsharing.hongqu.portal.api.entity.ShopWallet;
import com.channelsharing.hongqu.portal.api.service.*;
import lombok.Synchronized;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by liuhangjun on 2018/8/2.
 */
@Service
public class ShopProfitWalletServiceImpl implements ShopProfitWalletService {
    
    @Autowired
    private ShopProfitService shopProfitService;
    @Autowired
    private ShopWalletService shopWalletService;
    @Autowired
    private ShopInfoService shopInfoService;
    @Autowired
    private ConfigParamService configParamService;
    
    private final byte[] lock = new byte[1];
    
    /**
     * 找出店铺收益中确认时间超过阀值的数据，修改其状态为可提现
     * 然后把可提现的金额加入到店铺的钱包中
     */
    @Synchronized
    @Transactional
    @Override
    public void calculateProfitToWallet() {
        String days = configParamService.findOne(ConfigParamConstant.PORTAL_ORDER_PROFIT_CONFIRM_DAYS);
        int daysInt = NumberUtils.toInt(days, 10);
        
        String daysRange = configParamService.findOne(ConfigParamConstant.PORTAL_ORDER_PROFIT_CONFIRM_DAYS_RANGE);
        int daysRangeInt = NumberUtils.toInt(daysRange, 10);
        
        int num = 100;  // 一次最多处理100个订单
        Date beginTime = DateUtils.minusSecond(daysInt * ExpireTimeConstant.ONE_DAY + daysRangeInt * ExpireTimeConstant.ONE_DAY);  // 前daysInt天 + daysRangeInt天
        Date endTime = DateUtils.minusSecond(daysInt * ExpireTimeConstant.ONE_DAY);    // 前daysInt天
        ShopProfit query = new ShopProfit();
        query.setLimit(num);
        query.setConfirmFlag(BooleanEnum.yes.getCode());
        query.setAvailableFlag(BooleanEnum.no.getCode());
        query.setBeginConfirmTime(beginTime);
        query.setEndConfirmTime(endTime);
        
        Paging<ShopProfit> shopProfitPaging = shopProfitService.findPaging(query);
        if (shopProfitPaging != null) {
            List<ShopProfit> shopProfitList = shopProfitPaging.getRows();
            for (ShopProfit shopProfit : shopProfitList) {
                
                // 更新为可以提现的状态
                ShopProfit update = new ShopProfit();
                update.setId(shopProfit.getId());
                update.setAvailableFlag(BooleanEnum.yes.getCode());
                shopProfitService.modify(update);
                
                Long shopId = shopProfit.getShopId();
                // 加上钱包的收益
                ShopWallet shopWallet = shopWalletService.findOneByShopId(shopId);
                
                if (shopWallet == null) {
                    Long userId = shopInfoService.findOne(shopId).getUserId();
                    shopWallet = new ShopWallet();
                    shopWallet.setShopId(shopId);
                    shopWallet.setUserId(userId);
                    shopWallet.setProfitAmount(shopProfit.getProfit());
                    shopWallet.setBalance(shopProfit.getProfit());
                    shopWallet.setWithdraw(BigDecimal.ZERO);
                    shopWalletService.add(shopWallet);
                } else {
                    BigDecimal balance = shopWallet.getBalance().add(shopProfit.getProfit());
                    BigDecimal profitAmount = shopWallet.getProfitAmount().add(shopProfit.getProfit());
                    ShopWallet updateShopWallet = new ShopWallet();
                    updateShopWallet.setId(shopWallet.getId());
                    updateShopWallet.setShopId(shopId);
                    updateShopWallet.setBalance(balance);
                    updateShopWallet.setProfitAmount(profitAmount);
                    
                    shopWalletService.modify(updateShopWallet);
                    
                }
            }
        }
        
    }
}
