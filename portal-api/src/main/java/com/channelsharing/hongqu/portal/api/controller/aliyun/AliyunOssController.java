package com.channelsharing.hongqu.portal.api.controller.aliyun;

import com.aliyun.oss.OSSClient;
import com.channelsharing.cloud.aliyun.oss.OssUtil;
import com.channelsharing.hongqu.portal.api.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Api(tags = "阿里云OSS相关接口")
@RestController
@RequestMapping("/v1/aliyun/oss")
public class AliyunOssController extends BaseController {

	//bean在OSSClientConfig类中进行初始化
	@Autowired
	private OSSClient ossClient;

	@Autowired
	private OssUtil ossUtil;

	@Value("${aliyun.accessKeyId}")
	private String accessKeyId;

	@Value("${aliyun.oss.bucket}")
	private String bucket;

	@Value("${aliyun.oss.endpoint}")
	private String endpoint;

	@Value("${aliyun.oss.host}")
	private String host;

	private static final long FILE_MAX_LENGTH = 5 * FileUtils.ONE_MB;

	@ApiOperation("获取OSS上传凭证")
	@GetMapping("/policies")
	public Map<String, String> getOSSPolicy() throws UnsupportedEncodingException {

		return ossUtil.getOSSPolicy(FILE_MAX_LENGTH, accessKeyId, host);
	}

}
