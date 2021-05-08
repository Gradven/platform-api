package com.channelsharing.cloud.aliyun.oss;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSClientConfig {
	@Value("${aliyun.oss.endpoint}")
	private String endpoint;

	@Value("${aliyun.accessKeyId}")
	private String accessKeyId;

	@Value("${aliyun.accessKeySecret}")
	private String secretAccessKey;

	@Bean
	public OSSClient ossClientConfigurer() {
		return new OSSClient(endpoint, accessKeyId, secretAccessKey);
	}
}
