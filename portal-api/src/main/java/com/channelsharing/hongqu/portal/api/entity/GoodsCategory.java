/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.channelsharing.common.entity.BaseEntity;

/**
 * 商品分类Entity
 * 
 * @author liuhangjun
 * @version 2018-06-27
 */
@Data
public class GoodsCategory extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long parentId; // 父节点
	private String parentIds; // 节点路径
	private String name; // 分类名称
	private String code; // 分类编码
	private String picUrl; // 分类图片
	private String remarks; // 备注

	private List<GoodsCategory> childCategories = new ArrayList<>(); // 子节点

	public GoodsCategory() {
		super();
	}
}
