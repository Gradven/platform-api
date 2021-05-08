package com.channelsharing.hongqu.portal.api.controller.weixin;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.channelsharing.hongqu.portal.api.service.WeixinApiService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "微信开放接口")
@RestController
@RequestMapping(value = "/v1/weixin/apis")
public class WeixinApiController {
	@Resource
	private WeixinApiService weixinApiService;

	@ApiOperation(value = "获取小程序码")
	@PostMapping("/getwxacodeunlimit")
	public String getwxacodeunlimit(@RequestBody String body) {
		String fileKey = DigestUtils.md5Hex(body);

		return weixinApiService.getwxacodeunlimit(fileKey, body);
	}
}