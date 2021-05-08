/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.shop;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.service.ShopProfitService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.ShopSalesData;
import com.channelsharing.hongqu.portal.api.entity.ShopWallet;
import com.channelsharing.hongqu.portal.api.service.ShopSalesDataService;
import com.channelsharing.hongqu.portal.api.service.ShopWalletService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 店铺钱包Controller
 *
 * @author liuhangjun
 * @version 2018-06-26
 */
@Api(tags = "店铺钱包操作接口")
@Validated
@RestController
@RequestMapping("/v1/shopWallet")
public class ShopWalletController extends BaseController {
	@Resource
	private ShopWalletService shopWalletService;
	
	@Resource
	private ShopProfitService shopProfitService;

	@Resource
	private ShopSalesDataService salesDataService;

	@ApiOperation(value = "获取单条店铺钱包")
	@GetMapping("/")
	public ShopWallet findMyWallet() {
		Long shopId = super.currentUser().getShopId();

		if (shopId != null) {
			ShopWallet shopWallet = shopWalletService.findOneByShopId(shopId);
			if (shopWallet == null) {
				shopWallet = new ShopWallet();
				shopWallet.setShopId(shopId);
				shopWallet.setBalance(BigDecimal.ZERO);
				shopWallet.setWithdraw(BigDecimal.ZERO);
				shopWallet.setProfitAmount(BigDecimal.ZERO);
			}

			ShopSalesData shopSalesData = salesDataService.findByShopId(shopId);
			shopWallet.setShopSalesData(
					ObjectUtils.defaultIfNull(shopSalesData, new ShopSalesData(shopId, BigDecimal.ZERO, 0)));
			
			// 待入账收益  等所有收益上线后，以后废弃此段代码
			BigDecimal unAvailableProfit = shopProfitService.unAvailableProfit(super.currentUser().getShopId());
			shopWallet.setUnAvailableProfit(unAvailableProfit);
			
			// 所有收益
			BigDecimal allProfit = shopProfitService.allProfit(super.currentUser().getShopId());
			shopWallet.setAllProfit(allProfit);

			return shopWallet;
		} else {
			return new ShopWallet();
		}
	}
}
