/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.service;

import com.channelsharing.hongqu.portal.api.entity.WithdrawInfo;
import com.channelsharing.common.service.CrudService;

/**
 * 提现记录Service
 *
 * @author liuhangjun
 * @version 2018-06-26
 */
public interface WithdrawInfoService extends CrudService<WithdrawInfo> {
	public void withdraw(WithdrawInfo withdrawInfo);
}
