package com.channelsharing.hongqu.portal.api.service;

public interface WeixinApiService {
	/**
	 * 获取小程序码
	 * 
	 * @return
	 */
	public String getwxacodeunlimit(String fileKey, String body);
}