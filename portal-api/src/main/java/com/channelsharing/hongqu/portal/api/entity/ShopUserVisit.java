/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import java.util.Date;

import com.channelsharing.common.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopUserVisit extends BaseEntity {
	private static final long serialVersionUID = -287249091583942412L;
	private Long shopId; // 店铺id
	private Long userId; // 用户id
	private Date visitTime; // 最后访问时间
	private Integer visitCount; // 访问次数

	private String nickname;
	private String avatar;
}
