/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.withdraw;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现记录Entity
 *
 * @author liuhangjun
 * @version 2018-06-26
 */
@Data
public class WithdrawInfoAddRequestEntity {
	@ApiModelProperty(value = "提现金额，单位元，最多两位小数", example = "7.23")
	@NotNull(message = "提现金额不能为空")
	public String amount; // 提现金额

	@ApiModelProperty(value = "备注", example = "备注")
	@Length(min = 0, max = 128, message = "备注长度必须介于 0 和 128 之间")
	public String remark; // 备注
}
