/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.constant.Constant;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.wxpay.sdk.WXPayConstants;
import com.channelsharing.common.exception.ForbiddenException;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.service.CrudServiceImpl;
import com.channelsharing.hongqu.portal.api.dao.WithdrawInfoDao;
import com.channelsharing.hongqu.portal.api.entity.ShopWallet;
import com.channelsharing.hongqu.portal.api.entity.UserCertificate;
import com.channelsharing.hongqu.portal.api.entity.WithdrawInfo;
import com.channelsharing.hongqu.portal.api.service.ShopWalletService;
import com.channelsharing.hongqu.portal.api.service.UserCertificateService;
import com.channelsharing.hongqu.portal.api.service.WithdrawInfoService;
import com.channelsharing.hongqu.portal.api.weixin.WeixinPayUtil;

/**
 * 提现记录Service
 *
 * @author liuhangjun
 * @version 2018-06-26
 */
@Service
public class WithdrawInfoServiceImpl extends CrudServiceImpl<WithdrawInfoDao, WithdrawInfo>
		implements WithdrawInfoService {
	public static final String PORTAL_CACHE_PREFIX = Constant.PORTAL_CACHE_PREFIX;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

	@Resource
	private ShopWalletService shopWalletService;

	@Resource
	private UserCertificateService userCertificateService;

	@Resource
	private WeixinPayUtil weixinPayUtil;

	@Override
	public WithdrawInfo findOne(Long id) {
		WithdrawInfo entity = new WithdrawInfo();
		entity.setId(id);

		return super.findOne(entity);
	}

	@Override
	@Transactional
	@CacheEvict(value = PORTAL_CACHE_PREFIX
			+ "shopWallet", key = "#root.target.PORTAL_CACHE_PREFIX + 'shopWallet:shopId:' + #withdrawInfo.shopId")
	public void withdraw(WithdrawInfo withdrawInfo) {
		UserCertificate queryEntity = new UserCertificate();
		queryEntity.setUserId(withdrawInfo.getUserId());
		UserCertificate userCertificate = userCertificateService.findOne(queryEntity);
		if (userCertificate == null || userCertificate.getApproveStatus() == null
				|| userCertificate.getApproveStatus() != 1) {
			throw new ForbiddenException("请先完成实名认证");
		}

		ShopWallet shopWallet = shopWalletService.findOneByShopId(withdrawInfo.getShopId());

		if (shopWallet == null || shopWallet.getBalance() == null
				|| shopWallet.getBalance().compareTo(withdrawInfo.getAmount()) < 0) {
			throw new ForbiddenException("余额不足无法提现");
		}

		if (shopWallet.getWithdraw() == null) {
			shopWallet.setWithdraw(new BigDecimal(0));
		}

		shopWallet.setBalance(shopWallet.getBalance().subtract(withdrawInfo.getAmount()));
		shopWallet.setWithdraw(shopWallet.getWithdraw().add(withdrawInfo.getAmount()));

		shopWalletService.modify(shopWallet);

		withdrawInfo.setStatus(1);
		this.fillTradeNo(withdrawInfo);
		super.add(withdrawInfo);

		Map<String, String> respMap = null;
		try {
			respMap = weixinPayUtil.transfer(withdrawInfo.getUserId(),
					withdrawInfo.getAmount().multiply(ONE_HUNDRED).intValue(), withdrawInfo.getTradeNo(),
					withdrawInfo.getOpenId(), withdrawInfo.getClientIp(), "NO_CHECK", "商户提现");
		} catch (Exception e) {
			logger.error("withdraw exception, userId = " + withdrawInfo.getUserId(), e);
			throw new SystemInnerBusinessException("网络异常，转账失败");
		}

		String return_code = respMap.get("return_code");
		if (WXPayConstants.SUCCESS.equals(return_code)) {
			String result_code = respMap.get("result_code");
			if (WXPayConstants.SUCCESS.equals(result_code)) {
				// 转账成功之后记录下微信的订单号和时间，异常不能回滚
				try {
					withdrawInfo.setPaymentNo(respMap.get("payment_no"));
					withdrawInfo.setPaymentTime(respMap.get("payment_time"));
					super.modify(withdrawInfo);
				} catch (Exception e) {
					logger.error("modify withdrawInfo exception, userId = " + withdrawInfo.getUserId(), e);
				}

				return;
			} else {
				logger.error("transfer fail, userId = {}, openId = {}, resp = {}", withdrawInfo.getUserId(),
						withdrawInfo.getOpenId(), respMap);
				throw new SystemInnerBusinessException("提现失败，问题原因：" + respMap.get("err_code_des"));
			}
		} else {
			logger.error("transfer fail, userId = {}, openId = {}, resp = {}", withdrawInfo.getUserId(),
					withdrawInfo.getOpenId(), respMap);
			throw new SystemInnerBusinessException("提现失败，问题原因：" + respMap.get("return_msg"));
		}
	}

	private void fillTradeNo(WithdrawInfo withdrawInfo) {
		StringBuilder tradeNoBuilder = new StringBuilder(64);
		tradeNoBuilder.append(withdrawInfo.getType());
		tradeNoBuilder.append(DateTime.now().toString("yyyyMMddHHmmssSSS"));

		String userId = StringUtils.substring(withdrawInfo.getUserId().toString(), -10);

		tradeNoBuilder.append(StringUtils.repeat("0", 10 - userId.length()));
		tradeNoBuilder.append(userId);

		tradeNoBuilder.append(RandomUtils.nextInt(1000, 10000));

		withdrawInfo.setTradeNo(tradeNoBuilder.toString());
	}
}
