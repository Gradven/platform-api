/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.address;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 用户地址信息Entity
 * @author liuhangjun
 * @version 2018-07-16
 */
@Data
public class AddressInfoAddRequestEntity {
	
	@ApiModelProperty(value = "收货人", example = "李四")
	@Length(min=1, max=64, message="收货人长度必须介于 1 和 64 之间")
	private String consignee;   // 收货人
	
	@ApiModelProperty(value = "国家", example = "中国")
	@Length(min=1, max=64, message="国家长度必须介于 1 和 64 之间")
	public String country;		// 国家

	@ApiModelProperty(value = "省份", example = "浙江")
	@Length(min=1, max=64, message="省份长度必须介于 1 和 64 之间")
	public String province;		// 省份

	@ApiModelProperty(value = "城市", example = "杭州")
	@Length(min=1, max=64, message="城市长度必须介于 1 和 64 之间")
	public String city;		// 城市

	@ApiModelProperty(value = "街道", example = "文二西路")
	@Length(min=1, max=64, message="街道长度必须介于 0 和 64 之间")
	public String district;		// 街道

	@ApiModelProperty(value = "地址", example = "693号")
	@Length(min=1, max=255, message="地址长度必须介于 0 和 255 之间")
	public String address;		// 地址

	@ApiModelProperty(value = "手机号码", example = "13588887777")
	@Length(min=1, max=64, message="手机号码长度必须介于 0 和 64 之间")
	public String mobile;		// 手机号码

	@ApiModelProperty(value = "否默认地址：0:否，1:是", example = "1")
	@NotNull(message="是否默认地址：0:否，1:是不能为空")
	public Integer defaultFlag;		// 是否默认地址：0:否，1:是
	
	@ApiModelProperty(value = "收货时间类型，0：时间不限，1:节假日，2:周一至周五", example = "0")
	@Range(min=0, max=2, message="收货时间类型，0：时间不限，1:节假日，2:周一至周五")
	private Integer timeType;   // 收货时间类型



}
