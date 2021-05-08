/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.order;

import com.channelsharing.hongqu.portal.api.controller.invoice.InvoiceInfoAddRequestEntity;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单信息Entity
 * @author liuhangjun
 * @version 2018-06-20
 */
@Data
public class OrderInfoAddRequestEntity {

	@ApiModelProperty(value = "收货人", example = "张三")
	@NotNull(message = "收货人不能为空")
	@Length(min=1, max=64, message="收货人长度必须介于 1 和 64 之间")
	public String consignee;		// 收货人

	@ApiModelProperty(value = "国家", example = "中国")
	@NotNull(message = "国家不能为空")
	@Length(min=1, max=64, message="国家长度必须介于 1 和 64 之间")
	public String country;		// 国家

	@ApiModelProperty(value = "省份", example = "浙江")
	@NotNull(message = "省份不能为空")
	@Length(min=1, max=64, message="省份长度必须介于 1 和 64 之间")
	public String province;		// 省份

	@ApiModelProperty(value = "城市", example = "杭州")
	@NotNull(message = "城市不能为空")
	@Length(min=1, max=64, message="城市长度必须介于 1 和 64 之间")
	public String city;		// 城市

	@ApiModelProperty(value = "街区", example = "西溪小区")
	@NotNull(message = "街区不能为空")
	@Length(min=2, max=64, message="街区长度必须介于 1 和 64 之间")
	public String district;		//

	@ApiModelProperty(value = "地址", example = "天堂路11号")
	@NotNull(message = "址不能为空")
	@Length(min=0, max=255, message="地址长度必须介于 0 和 255 之间")
	public String address;		// 地址

	@ApiModelProperty(value = "手机号码", example = "13588455117")
	@Length(min=0, max=64, message="手机号码长度必须介于 0 和 64 之间")
	public String mobile;		// 手机号码
	
	@ApiModelProperty(value = "支付类型：1:微信支付，2:支付宝支付", example = "1")
	public Integer payType;		// 支付类型：1:微信支付，2:支付宝支付

	@ApiModelProperty(value = "备注", example = "我要给卖家留言")
	@Length(min=0, max=128, message="备注长度必须介于 0 和 128 之间")
	public String remark;		// 备注
	
	// 订购的产品list
	@NotNull
	public List<OrderGoodsAddRequestEntity> orderGoodsAddRequestEntityList;
	
	// 购物车id数组
	public Long[] cartIds;

}
