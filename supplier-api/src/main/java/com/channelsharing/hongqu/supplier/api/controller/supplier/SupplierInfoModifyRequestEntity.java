/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.supplier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 供应商信息Entity
 * @author liuhangjun
 * @version 2018-02-02
 */
@Data
public class SupplierInfoModifyRequestEntity {
	
	@ApiModelProperty(value = "供应商id", example="1")
	@NotNull(message = "供应商id不能为空")
	public Long id;

	@ApiModelProperty(value = "供应商名称", example="某某网络科技有限公司")
	@Length(min=1, max=64, message="供应商名称长度必须介于 1 和 64 之间")
	public String name;		// 供应商名称

	@ApiModelProperty(value = "统一社会信用代码", example="9154030268680600001")
	@Length(min=0, max=24, message="统一社会信用代码长度必须介于 0 和 24 之间")
	public String creditCode;		// 统一社会信用代码

	@ApiModelProperty(value = "营业执照图片", example="http://img.xxx.com")
	@Length(min=0, max=255, message="营业执照图片长度必须介于 0 和 255 之间")
	public String licenseImg;		// 营业执照图片

	@ApiModelProperty(value = "供应商法人", example="王某某")
	@Length(min=0, max=24, message="供应商法人长度必须介于 0 和 24 之间")
	public String legalRepresentative;		// 供应商法人

	@ApiModelProperty(value = "登记机关所在省份", example="浙江")
	@Length(min=0, max=2, message="登记机关所在省份长度必须介于 0 和 2 之间")
	public String regProvince;		// 登记机关所在省份

	@ApiModelProperty(value = "地址", example="杭州某某地")
	@Length(min=0, max=255, message="地址长度必须介于 0 和 255 之间")
	public String address;		// 地址
	
	@ApiModelProperty(value = "联系人", example="王某某")
	@Length(min=0, max=32, message="联系人长度必须介于 0 和 32 之间")
	public String contact;		// 联系人

	@ApiModelProperty(value = "联系人手机号码", example="13588888888")
	@Length(min=0, max=16, message="联系人手机号码长度必须介于 0 和 16 之间")
	public String mobile;		// 联系人手机号码

	@ApiModelProperty(value = "固定电话", example="0571-88888888")
	@Length(min=0, max=16, message="固定电话长度必须介于 0 和 16 之间")
	public String phone;		// 固定电话

	@Email
	@ApiModelProperty(value = "电子邮箱", example="xxx@xxx.com")
	@Length(min=0, max=128, message="电子邮箱长度必须介于 0 和 128 之间")
	public String email;		// 电子邮箱
	
	
	@ApiModelProperty(value = "客服电话", example="0571-88888888")
	@Length(min=0, max=16, message="固客服电话长度必须介于 0 和 16 之间")
	private String serviceTel; // 客服电话
	
	@ApiModelProperty(value = "客服微信号", example="weixinhao")
	@Length(min=0, max=128, message="客服微信号长度必须介于 0 和 128 之间")
	private String serviceWeixin;  // 客服微信号
	
	@ApiModelProperty(value = "客服QQ号", example="0571-88888888")
	@Length(min=0, max=24, message="客服QQ号长度必须介于 0 和 24 之间")
	private String serviceQQ;   // 客服QQ号
	
	@ApiModelProperty(value = "退换货地址", example="0571-88888888")
	@Length(min=0, max=255, message="退换货地址长度必须介于 0 和 255 之间")
	private String serviceAddress; // 退换货地址


}
