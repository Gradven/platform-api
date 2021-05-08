/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.order;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单发票Entity
 * @author liuhangjun
 * @version 2018-07-29
 */
@Data
public class OrderInvoiceAddRequestEntity {


	@ApiModelProperty(value = "订单编号", example = "")
	@Length(min=1, max=64, message="订单编号长度必须介于 1 和 64 之间")
	public String orderSn;		// 订单编号

	@ApiModelProperty(value = "用户id", example = "1")
	@NotNull(message="用户id不能为空")
	public Long userId;		// 用户id

	@ApiModelProperty(value = "发票类型，1:普通发票，2:增值税发票", example = "2")
	@NotNull(message="发票类型，1:普通发票，2:增值税发票不能为空")
	public Integer type;		// 发票类型，1:普通发票，2:增值税发票

	@ApiModelProperty(value = "抬头", example = "浙江华策娱乐科技有限公司")
	@Length(min=0, max=64, message="抬头长度必须介于 0 和 64 之间")
	public String title;		// 抬头

	@ApiModelProperty(value = "税号", example = "9879899ME899000")
	@Length(min=0, max=32, message="税号长度必须介于 0 和 32 之间")
	public String taxNo;		// 税号

	@ApiModelProperty(value = "开户银行", example = "工商银行")
	@Length(min=0, max=64, message="开户银行长度必须介于 0 和 64 之间")
	public String bankName;		// 开户银行

	@ApiModelProperty(value = "银行账号", example = "655355111113333")
	@Length(min=0, max=64, message="银行账号长度必须介于 0 和 64 之间")
	public String bankNo;		// 银行账号

	@ApiModelProperty(value = "企业地址", example = "浙江杭州")
	@Length(min=0, max=128, message="企业地址长度必须介于 0 和 128 之间")
	public String address;		// 企业地址

	@ApiModelProperty(value = "电话", example = "13912341234")
	@Length(min=0, max=64, message="电话长度必须介于 0 和 64 之间")
	public String telephone;		// 电话
	



}
