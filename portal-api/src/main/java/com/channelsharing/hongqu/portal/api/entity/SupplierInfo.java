/**
 * Copyright &copy; 2016-2022 供应商信息 All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 供应商信息Entity
 * @author 供应商信息
 * @version 2018-08-08
 */
@Data
public class SupplierInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private String name;		// 企业名称
	@JsonIgnore
	private String creditCode;		// 统一社会信用代码
	@JsonIgnore
	private String licenseImg;		// 营业执照图片
	@JsonIgnore
	private String legalRepresentative;		// 企业法人
	@JsonIgnore
	private String regProvince;		// 登记机关所在省份
	@JsonIgnore
	private String address;		// 地址
	@JsonIgnore
	private String contact;		// 联系人
	@JsonIgnore
	private String mobile;		// 联系人手机号码
	@JsonIgnore
	private String phone;		// 固定电话
	@JsonIgnore
	private String email;		// 电子邮箱
	@JsonIgnore
	private Integer status;		// 状态:0审核中 1已审核 2 审核不通过
	private String serviceTel;		// 客服电话
	private String serviceWeixin;		// 客服微信号
	private String serviceQq;		// 客服qq号
	private String serviceAddress;		// 售后地址


	public SupplierInfo() {
		super();
	}


}
