/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;

import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

/**
 * 商品分类Entity
 * @author liuhangjun
 * @version 2018-06-07
 */
@Data
public class GoodsCategory extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long parentId;		// 父节点
	private String parentIds;		// 节点路径
	private String name;		// 分类名称
	private String code;		// 分类编码
	private String remark;		// 备注
	
	private Boolean lastFlag;  // 是否是最后一级子节点，true：是，false：否；


	public GoodsCategory() {
		super();
	}


}
