package com.channelsharing.cloud.aliyun.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.channelsharing.cloud.constant.Constant;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liuhangjun on 2017/11/29.
 */
@Configuration
public class OssUtil {
    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
    // 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
    // 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
    // endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
    // 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。

    Long EXPIRED_TIME = 3600L;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.host}")
    private String host;

    @Value("${aliyun.oss.bucket}")
    private String defaultBucket;

    @Autowired
    private OSSClient ossClient;


    public String uploadFile(File file, String key) throws FileNotFoundException {
        InputStream stream = new FileInputStream(file);

        return this.uploadFile(stream, key);
    }


    public String uploadFile(InputStream stream, String key) {
        return this.uploadFile(defaultBucket, stream, key);
    }


    public String uploadFile(String bucketName, InputStream stream, String key) {

        if (StringUtils.isBlank(bucketName)) {
            bucketName = defaultBucket;
        }

        ossClient.putObject(bucketName, key, stream);

        if (!org.apache.commons.lang.StringUtils.startsWith(endpoint, Constant.HTTP)) {
            endpoint = Constant.HTTP_PREFIX + endpoint;
        }

        String uri;
        if (StringUtils.isEmpty(host)) {
            throw new SystemInnerBusinessException("Please config aliyun host of oss");
        } else {
            uri = host + "/" + key;
        }


        return uri;
    }

    public void deleteFile(String bucketName, String key) {

        if (StringUtils.isBlank(bucketName)) {
            bucketName = defaultBucket;
        }

        ossClient.deleteObject(bucketName, key);

    }

    public String getPrivateUri(String bucketName, String key, Long expiredTime) {

        if (StringUtils.isBlank(bucketName)) {
            bucketName = defaultBucket;
        }

        if (expiredTime == null) {
            expiredTime = EXPIRED_TIME;
        }

        Date expiration = Date
                .from(LocalDateTime.now().plusSeconds(expiredTime).atZone(ZoneId.systemDefault()).toInstant());

        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);

        return url.toString();
    }


    public Map<String, String> getOSSPolicy(Long fileMaxLength, String accessKeyId, String host) throws UnsupportedEncodingException {
        String dir = this.generateUploadDir(); // 限制上传目录，目录必须斜杠结尾
        Date expiration = Date.from(LocalDateTime.now().plusSeconds(30L).atZone(ZoneId.systemDefault()).toInstant());

        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, fileMaxLength);// 限制文件大小
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);// 限制上传目录

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        Map<String, String> ret = new LinkedHashMap<String, String>();
        ret.put("accessid", accessKeyId);
        ret.put("policy", encodedPolicy);
        ret.put("signature", postSignature);
        ret.put("dir", dir);
        ret.put("host", host);
        ret.put("expire", String.valueOf(expiration.getTime() / 1000));

        return ret;
    }


    private String generateUploadDir() {
        return new StringBuilder()
                .append("file")
                .append("/")
                .append(DateUtils.getShortDate())
                .append("/").toString();
    }
}
