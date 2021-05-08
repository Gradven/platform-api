/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.entity;


import com.channelsharing.common.entity.BaseEntity;
import lombok.Data;

/**
 * 供应商用户Entity
 * @author liuhangjun
 * @version 2018-02-02
 */
 @Data
public class SupplierUser extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String name;		// 供应商用户姓名
	private String password;		// 登录密码
	private Integer age;		// 年龄
	private String account;		// 登录名
	private String mobile;		// 手机号码
	private String email;		// 邮箱
	private Integer status;		// 用户状态
	private Long supplierId;		// 所属供应商
	private String remark;		// 备注


	public SupplierUser() {
		super();
	}



}
