package com.channelsharing.hongqu.portal.api.enums;

import com.channelsharing.common.enums.BaseEnum;

public enum WithdrawType implements BaseEnum {
	wechat(1, "微信");

	private Integer code;
	private String name;

	WithdrawType(Integer code, String name) {
		this.code = code;
		this.name = name;

	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}
}
