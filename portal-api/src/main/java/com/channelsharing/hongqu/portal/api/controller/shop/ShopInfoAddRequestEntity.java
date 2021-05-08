/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.shop;

import com.channelsharing.hongqu.portal.api.validations.Sensitive;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * 店铺信息Entity
 * @author liuhangjun
 * @version 2018-06-11
 */
@Data
public class ShopInfoAddRequestEntity {


	@Sensitive(message = "店铺名称不能包含敏感词")
	@ApiModelProperty(value = "店铺名称", example = "某某的店铺", name = "20个字，特殊符号不允许输入")
	@Length(min=1, max=32, message="店铺名称长度必须介于 1 和 32 之间")
	public String name;		// 店铺名称
	
	@ApiModelProperty(value = "店铺logo", example = "http://wwww.logo.com")
	@Length(min=1, max=255, message="店铺logo长度必须介于 1 和 255 之间")
	public String logo;		// 店铺logo
	
	@ApiModelProperty(value = "店铺介绍", example = "店铺介绍店铺介绍店铺介绍")
	@Length(min=0, max=255, message="店铺logo长度必须介于 0 和 255 之间")
	public String description;		// 店铺介绍
	
	@ApiModelProperty(value = "背景图url", example = "http://www.logo.com")
	@Length(min=0, max=255, message="背景图url长度必须介于 0 和 255 之间")
	public String backgroundUrl;		// 背景图url

}
