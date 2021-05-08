/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.region;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.service.RegionInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.RegionInfo;
import com.channelsharing.common.entity.Paging;


/**
 * 国家地区信息Controller
 * @author liuhangjun
 * @version 2018-07-16
 */
@Api(tags = "国家地区信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/regionInfo")
public class RegionInfoController extends BaseController {

	@Resource
	private RegionInfoService regionInfoService;
	

	@ApiOperation(value = "根据parentCode获取国家地区信息列表")
	@GetMapping
	public Paging<RegionInfo> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit,
			@RequestParam String parentCode){

		RegionInfo entity = new RegionInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setParentCode(parentCode);

		return regionInfoService.findPaging(entity);

	}
	

}
