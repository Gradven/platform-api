/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.address;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.service.AddressInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.portal.api.entity.AddressInfo;
import com.channelsharing.common.entity.Paging;


/**
 * 用户地址信息Controller
 * @author liuhangjun
 * @version 2018-07-16
 */
@Api(tags = "用户地址信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/addressInfo")
public class AddressInfoController extends BaseController {

	@Resource
	private AddressInfoService addressInfoService;
	

	@ApiOperation(value = "获取用户地址信息列表")
	@GetMapping
	public Paging<AddressInfo> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit){

		AddressInfo entity = new AddressInfo();
		entity.setOffset(offset);
		entity.setLimit(limit);
		entity.setUserId(super.currentUserId());

		return addressInfoService.findPaging(entity);

	}

	@ApiOperation(value = "提交一条用户地址信息")
	@PostMapping
	public void add(@RequestBody @Validated AddressInfoAddRequestEntity addressInfoAddRequestEntity){
		AddressInfo entity = new AddressInfo();
		BeanUtils.copyProperties(addressInfoAddRequestEntity, entity);
		entity.setUserId(super.currentUserId());

		addressInfoService.add(entity);
	}

	@ApiOperation(value = "修改一条用户地址信息")
	@PutMapping
	public void modify(@RequestBody @Validated AddressInfoModifyRequestEntity addressInfoModifyRequestEntity){
        AddressInfo entity = new AddressInfo();
		BeanUtils.copyProperties(addressInfoModifyRequestEntity, entity);
		entity.setUserId(super.currentUserId());
		addressInfoService.modify(entity);
	}

	@ApiOperation(value = "删除一条用户地址信息")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
	    AddressInfo entity = new AddressInfo();
	    entity.setId(id);
		entity.setUserId(super.currentUserId());
		addressInfoService.delete(entity);
	}

}
