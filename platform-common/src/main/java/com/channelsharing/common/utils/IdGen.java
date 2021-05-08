/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.channelsharing.common.utils;

import java.util.UUID;

/**
 * @author liuhangjun
 */
public class IdGen {

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 * 长度为32位
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}


	public static void main(String[] args) {
		System.out.println(IdGen.uuid());
		System.out.println(IdGen.uuid().length());
	}

}
