/**
 * Copyright &copy; 2016-2022 liuahangjun All rights reserved.
 */
package com.channelsharing.hongqu.supplier.api.controller.user;

import javax.annotation.Resource;

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.entity.UserInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.channelsharing.common.entity.Paging;
import com.channelsharing.hongqu.supplier.api.service.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 用户信息Controller
 * @author liuahangjun
 * @version 2018-03-12
 */
@Api(tags = "用户信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/userInfo")
public class UserInfoController extends BaseController {

	@Resource
	private UserInfoService userInfoService;

    @ApiOperation(value = "获取单条用户信息")
	@GetMapping("/{id}")
	public UserInfo findOne(@PathVariable Long id){
		
	    UserInfo userInfo = userInfoService.findOne(id);
	    if (userInfo== null)
			userInfo =  new UserInfo();

		return userInfo;
	}

	@ApiOperation(value = "获取用户信息列表")
	@GetMapping
	public Paging<UserInfo> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit){

		UserInfo entity = new UserInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);

		return userInfoService.findPaging(entity);

	}


}
