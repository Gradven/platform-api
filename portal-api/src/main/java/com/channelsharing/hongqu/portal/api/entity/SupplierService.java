/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 供应商售后服务Entity
 * @author liuhangjun
 * @version 2018-08-08
 */
@Data
public class SupplierService extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long orderGoodsId;		// 订单商品id
	private Long supplierId;		// 供应商id
	private Long shopId;
	private Long userId;		// 用户id
	private Integer status;		// 退货状态，1:申请退货，2:审核通过，3:审核不通过过，4：完成退换货，99:取消退换货
	private Integer reasonType;		// 退货理由，1：发错货，2:尺码偏大，3:尺码偏小，4:七天无理由，5:商品不适合，6:不喜欢，99:其他
	private String reason;		// 其他理由
	private Date cancelTime;		// 取消退货时间
	private String serviceAddress;		// 寄回地址

	private OrderGoods orderGoods;

	public SupplierService() {
		super();
	}


}
