/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import java.util.List;

import lombok.Data;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 页面区块信息Entity
 * @author liuhangjun
 * @version 2018-07-26
 */
@Data
public class PageFragment extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long pageId;		// 页面id
	private String keyword;		// 关键字
	private Integer type;		// 区块类型（1:商品，2:商品分类，3:店铺）
	private String name;		// 区块名称
	private String value;		// 区块值
	private String remark;		// 备注
	
	// object对应type的对象
	private List<Object> objectList;


	public PageFragment() {
		super();
	}


}
