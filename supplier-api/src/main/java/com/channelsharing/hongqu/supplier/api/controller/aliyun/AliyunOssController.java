package com.channelsharing.hongqu.supplier.api.controller.aliyun;

import com.aliyun.oss.OSSClient;
import com.channelsharing.cloud.constant.Constant;
import com.channelsharing.cloud.aliyun.oss.OssUtil;
import com.channelsharing.common.utils.DownloadImageUtil;
import com.channelsharing.common.utils.IdGen;
import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.common.utils.DateUtils;
import com.channelsharing.hongqu.supplier.api.enums.UEditorAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import net.sf.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "阿里云OSS相关接口")
@RestController
@RequestMapping("/v1/aliyun/oss")
public class AliyunOssController extends BaseController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //bean在OSSClientConfig类中进行初始化
    @Autowired
    private OSSClient ossClient;

    @Autowired
    private OssUtil ossUtil;


    @Autowired
    private HttpServletRequest request;


    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.bucket}")
    private String bucket;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.host}")
    private String host;

    private static final long FILE_MAX_LENGTH = 10 * FileUtils.ONE_MB;

    @ApiOperation("获取OSS上传凭证")
    @GetMapping("/policies")
    public Map<String, String> getOSSPolicy() throws UnsupportedEncodingException {

        return ossUtil.getOSSPolicy(FILE_MAX_LENGTH, accessKeyId, host);

    }

    @ApiOperation("uEditor请求类型判断")
    @GetMapping(value = "/uEditor/upload/img")
    @ResponseBody
    public Map uEditorUpload(@RequestParam(required = false) String callback,
                             @RequestParam(required = false) UEditorAction action,
                             @RequestParam(required = false, name = "source") String[] source) {

        logger.debug("Receive img upload request, callback value is {}", callback);

        String requestUrl = request.getRequestURL().toString();
        String protocolKey = "";
        if (StringUtils.startsWith(requestUrl, Constant.HTTP)) {
            protocolKey = Constant.HTTP_PREFIX;
        } else {
            protocolKey = Constant.HTTPS_PREFIX;
        }

        String serveName = request.getServerName();
        Integer serverPort = request.getServerPort();

        String urlDomain = protocolKey + serveName + ":" + Integer.toString(serverPort);

        Map<String, Object> map = new HashMap();

        String[] imgTypeArray = {".png", ".jpg", ".jpeg", ".gif", ".bmp"};
        String[] domainArray = {"127.0.0.1", "localhost", "img.baidu.com"};
        Integer imageMaxSize = 5102000;
        String imagePathFormat = "ueditor/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}";

        if (action == UEditorAction.config) {

            map.put("imageUrl", urlDomain + "/uEditor/upload/img?action=" + UEditorAction.uploadimage.getName());
            map.put("imagePath", "/ueditor/img/");
            map.put("imageAllowFiles", imgTypeArray);

            //============================================

            map.put("catcherLocalDomain", domainArray);
            map.put("catcherActionName", UEditorAction.catchimage.getName());  /* 执行抓取远程图片的action名称 */
            map.put("catcherFieldName", "source");  /* 提交的图片列表表单名称 */
            map.put("catcherPathFormat", imagePathFormat);  /* 上传保存路径,可以自定义保存路径和文件名格式 */
            map.put("catcherUrlPrefix", "");  /* 图片访问路径前缀 */
            map.put("catcherMaxSize", imageMaxSize);
            map.put("catcherAllowFiles", imgTypeArray);

            //============================================

            map.put("imageActionName", UEditorAction.uploadfile.getName()); /* 执行上传图片的action名称 */
            map.put("imageFieldName", "upfile");   /* 提交的图片表单名称 */
            map.put("imageMaxSize",  imageMaxSize); /* 上传大小限制，单位B */
            map.put("imageAllowFiles", imgTypeArray);  /* 上传图片格式显示 */
            map.put("imageCompressEnable", true);
            map.put("imageCompressBorder", 1600); /* 图片压缩最长边限制 */
            map.put("imageInsertAlign", "none"); /* 插入的图片浮动方式 */
            map.put("imageUrlPrefix", "");  /* 图片访问路径前缀 */
            map.put("imagePathFormat", imagePathFormat); /* 上传保存路径,可以自定义保存路径和文件名格式 */

        } else if (action == UEditorAction.catchimage ) {

            String localPath = "/tmp/image/";

            if (source != null){
                List list = new ArrayList<>();
                for (String imgSource : source){

                    CatchImage catchImage = new CatchImage();

                    // 去掉微信多余的参数，此些参数会影响图片的上传
                    String imgSourceCopy = StringUtils.replaceAll(imgSource, "wxfrom", "1");
                    imgSourceCopy = StringUtils.replaceAll(imgSourceCopy, "wx_lazy", "1");
                    imgSourceCopy = StringUtils.replaceAll(imgSourceCopy, "&tp=webp", "");

                    catchImage.setSource(imgSource);

                    File file = null;

                    try {
                        
                        file = DownloadImageUtil.downloadWithHttp(imgSourceCopy, localPath);
                        String fileName = file.getName();
                        String fileUrl = ossUtil.uploadFile(file, "uEditor/" + DateUtils.getShortDate() + "/" + fileName);
                        catchImage.setUrl(fileUrl);

                        catchImage.setState("SUCCESS");
                    } catch (FileNotFoundException e) {
                        catchImage.setState("Failed");
                        e.printStackTrace();
                    } catch (IOException e) {
                        catchImage.setState("Failed");
                        e.printStackTrace();
                    }


                    list.add(catchImage);
                    
                    if (file != null)
                       com.channelsharing.common.utils.FileUtils.delFile(file.getAbsolutePath());

                }

                Map listMap = new HashMap();
                listMap.put("list", list);

                JSONArray jsonarray = JSONArray.fromObject(listMap);

                String resultStr = jsonarray.toString();
                resultStr = StringUtils.substring(resultStr, 1, resultStr.length() - 1);

                map.put("responseText", resultStr);

            }

        }


        return map;
    }


    /**
     * UEDITOR文件上传
     *
     * @param request
     * @return UEDITOR 需要的json格式数据
     */
    @ApiOperation("uEditor文件上传")
    @PostMapping("/uEditor/upload/img")
    public String uEditorUpload(HttpServletRequest request, @RequestParam UEditorAction action) {

        Map<String, Object> resultMap = new HashMap<>();

        MultipartHttpServletRequest multipartHttpServletRequest = null;
        MultipartFile file = null;
        InputStream is = null;
        String fileName = "";

        // 原始文件名   UEDITOR创建页面元素时的alt和title属性
        String originalFileName;

        try {
            multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            // 从config.json中取得上传文件的ID
            file = multipartHttpServletRequest.getFile("upfile");


            // 取得文件的原始文件名称
            if (file != null && file.getSize() > 0 && file.getOriginalFilename().contains(".")) {

                originalFileName = file.getOriginalFilename();

                String suffix = StringUtils.split(file.getOriginalFilename(), ".")[StringUtils.split(file.getOriginalFilename(), ".").length - 1];

                String resKey = DateUtils.getShortDate() + "/" + IdGen.uuid() + "." + suffix;

                InputStream stream = file.getInputStream();

                String uri = ossUtil.uploadFile(stream, resKey);

                resultMap.put("state", "SUCCESS");// UEDITOR的规则:不为SUCCESS则显示state的内容
                resultMap.put("url", uri);
                resultMap.put("name", originalFileName);
                resultMap.put("size", file.getSize());
                resultMap.put("original", originalFileName);

            }


        } catch (Exception e) {
            resultMap.put("state", "文件上传失败!");
            resultMap.put("url", "");
            resultMap.put("name", "");
            resultMap.put("original", "");

            logger.error("Upload file {} failed!, error message is {} ", fileName, e.getMessage());
        }

        JSONArray json = JSONArray.fromObject(resultMap);
        String jsonString = json.toString();

        jsonString = StringUtils.substring(jsonString, 1, jsonString.length() - 1);

        return jsonString;
    }

    @Data
    public class CatchImage{
        private String source;
        private String url;
        private String state;


    }
}
