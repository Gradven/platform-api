/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 商品运营推荐语Entity
 * @author liuhangjun
 * @version 2018-07-23
 */
@Data
public class RecommendSentence extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String recommend;		// 推荐语


	public RecommendSentence() {
		super();
	}


}