/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.withdraw;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.channelsharing.common.entity.Paging;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.exception.ForbiddenException;
import com.channelsharing.common.lock.method.CacheLock;
import com.channelsharing.common.lock.method.CacheParam;
import com.channelsharing.common.utils.CommonUtils;
import com.channelsharing.common.utils.CustomHttpHeaderUtil;
import com.channelsharing.hongqu.portal.api.constant.ConfigParamConstant;
import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.WithdrawInfo;
import com.channelsharing.hongqu.portal.api.enums.WithdrawType;
import com.channelsharing.hongqu.portal.api.service.ConfigParamService;
import com.channelsharing.hongqu.portal.api.service.WithdrawInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 提现记录Controller
 *
 * @author liuhangjun
 * @version 2018-06-26
 */
@Api(tags = "提现记录操作接口")
@Validated
@RestController
@RequestMapping("/v1/withdrawInfo")
public class WithdrawInfoController extends BaseController {
	@Resource
	private WithdrawInfoService withdrawInfoService;

	@Resource
	private ConfigParamService configParamService;

	@ApiOperation(value = "获取提现记录列表")
	@GetMapping
	public Paging<WithdrawInfo> findPaging(@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit) {
		WithdrawInfo entity = new WithdrawInfo();
		entity.setShopId(super.currentUser().getShopId());
		entity.setOffset(offset);
		entity.setLimit(limit);

		return withdrawInfoService.findPaging(entity);
	}

	@ApiOperation(value = "提现接口")
	@PostMapping
	@CacheLock(prefix = "portal:withdraw:")
	public void withdraw(
			@CacheParam(name = "withdrawInfoAddRequestEntity") @RequestBody @Validated WithdrawInfoAddRequestEntity withdrawInfoAddRequestEntity) {
		UserInfo currentUser = super.currentUser();

		if (currentUser.getShopId() == null) {
			throw new ForbiddenException("您未开店不能提现");
		}

		if (currentUser.getThirdPartyUserId() == null) {
			throw new ForbiddenException("账户异常，请联系客服解决");
		}

		if (!NumberUtils.isParsable(withdrawInfoAddRequestEntity.getAmount())) {
			throw new BadRequestException("提现金额格式错误");
		}

		if (StringUtils.startsWith(withdrawInfoAddRequestEntity.getAmount(), "-")) {
			throw new BadRequestException("提现金额不能为负数");
		}

		if (StringUtils.substringAfterLast(withdrawInfoAddRequestEntity.getAmount(), ".").length() > 2) {
			throw new BadRequestException("提现金额最多两位小数");
		}

		BigDecimal amount = new BigDecimal(withdrawInfoAddRequestEntity.getAmount());
		String minWithdrawAmount = configParamService.findOne(ConfigParamConstant.PORTAL_WITHDRAW_MIN_AMOUNT);
		if (amount.intValue() < NumberUtils.toInt(minWithdrawAmount, 10)) {
			throw new BadRequestException("提现金额不能少于" + NumberUtils.toInt(minWithdrawAmount, 10) + "元");
		}

		String clientIp = CustomHttpHeaderUtil.getRemoteIp();

		WithdrawInfo entity = new WithdrawInfo();
		entity.setAmount(amount);
		entity.setRemark(withdrawInfoAddRequestEntity.getRemark());
		entity.setShopId(currentUser.getShopId());
		entity.setUserId(currentUser.getId());
		entity.setType(WithdrawType.wechat.getCode());
		entity.setOpenId(currentUser.getThirdPartyUserId());
		entity.setClientIp(CommonUtils.IsIpv4(clientIp) ? clientIp : "115.238.51.194");

		withdrawInfoService.withdraw(entity);
	}
}
