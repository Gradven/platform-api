/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.entity;


import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

import java.lang.annotation.Annotation;

/**
 * 敏感词信息Model
 * @author liuhangjun
 * @version 2017-07-10
 */
@Data
public class SensitiveWord extends BaseEntity implements Annotation {

	private static final long serialVersionUID = 1L;
	private String word;		// 敏感词


	public SensitiveWord() {
		super();
	}


	@Override
	public Class<? extends Annotation> annotationType() {
		return null;
	}
}
