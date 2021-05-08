/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.user;

import javax.annotation.Resource;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.channelsharing.hongqu.portal.api.entity.UserCertificate;
import com.channelsharing.hongqu.portal.api.service.UserCertificateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户认证信息Controller
 *
 * @author liuhangjun
 * @version 2018-07-12
 */
@Api(tags = "用户认证信息操作接口")
@Validated
@RestController
@RequestMapping("/v1/userCertificate")
public class UserCertificateController extends BaseController {

	@Resource
	private UserCertificateService userCertificateService;

	@ApiOperation(value = "获取当前用户自己的认证信息")
	@GetMapping
	public UserCertificate findOne() {
		UserCertificate entity = new UserCertificate();
		entity.setUserId(super.currentUserId());

		UserCertificate userCertificate = userCertificateService.findOne(entity);
		return userCertificate;
	}

	@ApiOperation(value = "提交一条用户认证信息")
	@PostMapping
	public UserCertificate add(
			@RequestBody @Validated UserCertificateAddRequestEntity userCertificateAddRequestEntity) {
		UserCertificate entity = new UserCertificate();
		BeanUtils.copyProperties(userCertificateAddRequestEntity, entity);
		entity.setUserId(super.currentUserId());

		userCertificateService.add(entity);

		return userCertificateService.findOne(entity);
	}

	@ApiOperation(value = "修改一条用户认证信息")
	@PutMapping
	public UserCertificate modify(
			@RequestBody @Validated UserCertificateModifyRequestEntity userCertificateModifyRequestEntity) {
		UserCertificate entity = new UserCertificate();
		BeanUtils.copyProperties(userCertificateModifyRequestEntity, entity);
		entity.setUserId(super.currentUserId());

		userCertificateService.modify(entity);

		return userCertificateService.findOne(entity);
	}
}
