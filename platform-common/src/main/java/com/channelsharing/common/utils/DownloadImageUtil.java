package com.channelsharing.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuhangjun on 2018/4/16.
 */

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author liuhangjun
 * @version 1.0
 * @说明 从网络获取图片到本地
 */
public class DownloadImageUtil {
    /**
     * 测试
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String url = "http://mmbiz.qpic.cn/mmbiz_png/rmhhIb0HWb4UqhKibAyjCjthtgkTAGASdo6cVEwDx9qh5OHDpofPTE0NNsD5tjMOpTS4LXfeXK3f0IU459U2eHA/640?";
        DownloadImageUtil.downloadWithHttp(url, "aaaaaaaa/");
    }
    
    public static File downloadWithHttp(String url, String localDir) throws IOException {
        Request request = new Request.Builder().get().url(url).build();
        Response response = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).build().newCall(request).execute();
        
        byte[] btImg = response.body().bytes();
        if (null != btImg && btImg.length > 0) {
            String contentType = response.header("Content-Type");
            MediaType mediaType = MediaType.parse(contentType);
            String suffix = mediaType != null ? mediaType.subtype() : getSuffixFromUrl(url);
            
            String fileName = "/" + IdGen.uuid() + "." + suffix;
            
            return writeImageToDisk(btImg, fileName, localDir);
            
        } else {
            return null;
        }
    }
    
    /**
     * 将图片写入到磁盘
     *
     * @param img
     *            图片数据流
     * @param fileName
     *            文件保存时的名称
     */
    public static File writeImageToDisk(byte[] img, String fileName, String localDir) {
        try {
            
            String rootPath = ResourceUtils.getURL("classpath:").getPath();
            
            File dir = new File(rootPath + localDir);
            
            if (!dir.exists())
                dir.mkdirs();
            
            File file = new File(rootPath + localDir + fileName);
            
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(img);
            fops.flush();
            fops.close();
            
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl
     *            网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 从输入流中获取数据
     *
     * @param inStream
     *            输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 5];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
    
    /**
     * 获取url中文件的后缀名
     *
     * @param url
     * @return
     */
    private static String getSuffixFromUrl(String url) {
        
        String suffixes = "avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc";
        Pattern pat = Pattern.compile("[\\w]+[\\.](" + suffixes + ")");// 正则判断
        Matcher mc = pat.matcher(url);// 条件匹配
        
        String suffix = null;
        String name;
        while (mc.find()) {
            name = mc.group();// 截取文件名后缀名
            
            suffix = StringUtils.split(name, ".")[1];
            
            return suffix;
        }
        
        if (suffix == null) {
            if (StringUtils.contains(url, "wx_fmt=")) {
                
                pat = Pattern.compile("[\\w]+[\\=](" + suffixes + ")");// 正则判断
                mc = pat.matcher(url);// 条件匹配
                
                while (mc.find()) {
                    
                    name = mc.group();// 截取文件名后缀名
                    
                    suffix = StringUtils.split(name, "=")[1];
                    
                    return suffix;
                }
                
            }
            
        }
        
        return StringUtils.defaultIfEmpty(suffix, "jpg");
    }
}
