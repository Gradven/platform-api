/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.invoice;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 发票信息Entity
 * @author liuhangjun
 * @version 2018-07-29
 */
@Data
public class InvoiceInfoModifyRequestEntity {

    @ApiModelProperty(value = "id", example = "1")
    @NotNull
	public Long id;
	
	
	@ApiModelProperty(value = "发票类型", example = "2")
	@NotNull(message="发票类型，1:普通发票，2:增值税发票不能为空")
	public Integer type;		// 发票类型，1:普通发票，2:增值税发票
	
	@ApiModelProperty(value = "抬头", example = "湖南某科技有限公司")
	@Length(min=0, max=64, message="抬头长度必须介于 0 和 64 之间")
	public String title;		// 抬头
	
	@ApiModelProperty(value = "抬头类型", example = "1")
	@Range(min=1, max=2, message="抬头只必须介于 1 和 2 之间")
	public Integer titleType;		// 抬头类型
	
	@ApiModelProperty(value = "税号", example = "919291219921921M3E")
	@Length(min=0, max=32, message="税号长度必须介于 0 和 32 之间")
	public String taxNo;		// 税号
	
	@ApiModelProperty(value = "开户银行", example = "湖南长沙招商银行")
	@Length(min=0, max=128, message="开户银行长度必须介于 0 和 128 之间")
	public String bankName;		// 开户银行
	
	@ApiModelProperty(value = "银行账号", example = "9651111111111")
	@Length(min=0, max=64, message="银行账号长度必须介于 0 和 64 之间")
	public String bankNo;		// 银行账号
	
	@ApiModelProperty(value = "企业地址", example = "湖南长沙某街道某门派")
	@Length(min=0, max=128, message="企业地址长度必须介于 0 和 128 之间")
	public String address;		// 企业地址
	
	@ApiModelProperty(value = "电话", example = "13812341234")
	@Length(min=0, max=64, message="电话长度必须介于 0 和 64 之间")
	public String telephone;		// 电话
	
	@ApiModelProperty(value = "是否默认：0:否，1:是", example = "1")
	public Integer defaultFlag;		// 是否默认：0:否，1:是



}
