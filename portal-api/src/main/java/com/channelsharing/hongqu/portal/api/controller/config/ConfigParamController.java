/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.config;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.service.ConfigParamService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 系统配置项Controller
 * @author liuhangjun
 * @version 2018-06-17
 */
@Api(tags = "系统配置项操作接口")
@Validated
@RestController
@RequestMapping("/v1/configParam")
public class ConfigParamController extends BaseController {

	@Resource
	private ConfigParamService configParamService;

    @ApiOperation(value = "获取单条系统配置项")
	@GetMapping("/key")
	public String findOne(@RequestParam String key){

	    String configParam = configParamService.findOne(key);

		return configParam;
	}
	
	
}
