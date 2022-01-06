package com.zorn.taobao.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class TencentCOSUtil {
    // 存储桶名称
    private static final String bucketName = "taobao-1302520138";
    //secretId 秘钥id
    private static final String secretId = "AKIDf8MJ2SE55WWGkmn1FBtWInDOIwKVB9a6";
    //SecretKey 秘钥
    private static final String secretKey = "QikV1PN7HBwpiM1FG3LNOB4vlNqgIBpF";
    // 腾讯云 自定义文件夹名称
//    private static final String prefix;
    // 访问域名
    public static final String URL = "https://taobao-1302520138.cos.ap-guangzhou.myqcloud.com";
    // 创建COS 凭证
    private static COSCredentials credentials = new BasicCOSCredentials(secretId, secretKey);
    // 配置 COS 区域 就购买时选择的区域 我这里是 广州（guangzhou）
    private static ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));

    public static String uploadfile(MultipartFile file, String prefix, String fileName) {
        // 创建 COS 客户端连接
        COSClient cosClient = new COSClient(credentials, clientConfig);
        String originalFilename = file.getOriginalFilename();
        try {
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
            File localFile = File.createTempFile(String.valueOf(System.currentTimeMillis()),substring);
            file.transferTo(localFile);
            fileName = prefix + fileName + substring;
            // 将 文件上传至 COS
            PutObjectRequest objectRequest = new PutObjectRequest(bucketName, fileName, localFile);
            cosClient.putObject(objectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
        }
        return URL + fileName;
    }
}
