/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.recommend;

import javax.annotation.Resource;

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
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.service.RecommendSentenceService;
import com.channelsharing.hongqu.portal.api.entity.RecommendSentence;
import com.channelsharing.common.entity.Paging;


/**
 * 商品运营推荐语Controller
 * @author liuhangjun
 * @version 2018-07-23
 */
@Api(tags = "商品运营推荐语操作接口")
@Validated
@RestController
@RequestMapping("/v1/recommendSentence")
public class RecommendSentenceController extends BaseController {

	@Resource
	private RecommendSentenceService recommendSentenceService;
	
	@ApiOperation(value = "获取商品运营推荐语列表")
	@GetMapping
	public Paging<RecommendSentence> findPaging(
			@RequestParam(defaultValue = "0") Integer offset,
			@RequestParam(defaultValue = "10") Integer limit){

		RecommendSentence entity = new RecommendSentence();
		entity.setOffset(offset);
		entity.setLimit(limit);

		return recommendSentenceService.findPaging(entity);

	}
	

}
