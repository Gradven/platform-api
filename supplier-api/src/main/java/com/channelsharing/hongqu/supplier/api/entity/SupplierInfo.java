/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;


import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

/**
 * 供应商信息Entity
 * @author liuhangjun
 * @version 2018-02-02
 */
@Data
public class SupplierInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String name;		// 供应商名称
	private String creditCode;		// 统一社会信用代码
	private String licenseImg;		// 营业执照图片
	private String legalRepresentative;		// 供应商法人
	private String regProvince;		// 登记机关所在省份
	private String address;		// 地址
	private String contact;		// 联系人
	private String mobile;		// 联系人手机号码
	private String phone;		// 固定电话
	private String email;		// 电子邮箱
	private Integer status;		// 状态:0审核中 1已审核 2 审核不通过
	private String serviceTel; // 客服电话
	private String serviceWeixin;  // 客服微信号
	private String serviceQQ;   // 客服QQ号
	private String serviceAddress; // 客服退换货地址


	public SupplierInfo() {
		super();
	}



}
