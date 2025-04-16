package com.cjx.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import java.io.InputStream;

/**
 * 阿里云OSS文件上传工具类
 */
public class AliOssUtil {
    // OSS服务的Endpoint
    private static final String ENDPOINT = "https://oss-cn-shenzhen.aliyuncs.com";
    // OSS的访问密钥ID(见Mac电脑备忘录：阿里云文件存储oss）
    private static final String ACCESS_KEY_ID = "";
    // OSS的访问密钥秘钥
    private static final String SECRET_ACCESS_KEY = "";
    // OSS的存储空间名称
    private static final String BUCKET_NAME = "big-event-lsy";
    /**
     * 上传文件到阿里云OSS
     *
     * @param objectName 上传到OSS的文件名
     * @param inputStream 文件的输入流
     * @return 上传成功后文件的URL
     */
    public static String uploadFile(String objectName, InputStream inputStream) {
        OSS ossClient = (new OSSClientBuilder()).build(ENDPOINT, ACCESS_KEY_ID, SECRET_ACCESS_KEY);
        String url = "";

        try {
            // 创建存储空间
            ossClient.createBucket(BUCKET_NAME);
            // 上传文件
            ossClient.putObject(BUCKET_NAME, objectName, inputStream);
            // 构造文件URL
            String var10000 = ENDPOINT.substring(ENDPOINT.lastIndexOf("/") + 1);
            url = "https://" + BUCKET_NAME + "." + var10000 + "/" + objectName;
        } catch (OSSException var9) {
            // 处理OSS异常
            System.out.println("Caught an OSSException:");
            System.out.println("Error Message:" + var9.getErrorMessage());
            System.out.println("Error Code:" + var9.getErrorCode());
            System.out.println("Request ID:" + var9.getRequestId());
            System.out.println("Host ID:" + var9.getHostId());
        } catch (ClientException var10) {
            // 处理客户端异常
            System.out.println("Caught an ClientException:");
            System.out.println("Error Message:" + var10.getMessage());
        } finally {
            // 关闭OSS客户端
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return url;
    }
}
