/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.withdraw;

import java.math.BigDecimal;
import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 提现记录Entity
 * @author liuhangjun
 * @version 2018-06-26
 */
@Data
public class WithdrawInfoModifyRequestEntity {

    @ApiModelProperty(value = "id", example = "1")
    @NotNull
	public Long id;

	public Long userId;		// 提现人

	public Long shopId;		// 店铺id

	public BigDecimal amount;		// 提现金额

	public Integer status;		// 状态：1:提现成功，2：提现中，99:提现失败

	public Integer type;		// 提现类型：1:提现到微信，2:提现到银行卡


	@Length(min=0, max=128, message="备注长度必须介于 0 和 128 之间")
	public String remark;		// 备注



}
