package com.aisile.utils.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Setter
@PropertySource("classpath:aliyun.properties")
@Component
public class OSSClientUtil {

    @Value("${OSS_ACCESS_KEY_ID}")
    private static String OSS_ACCESS_KEY_ID;
    @Value("${OSS_ACCESS_KEY_SECRET}")
    private static String OSS_ACCESS_KEY_SECRET;
    @Value("${prefix}")
    private static String prefix;
    @Value("${bucket}")
    private static String bucket;
    private static String dir;

    @Value("${endpoint}")
    private static String endpoint;


    public static String uploadFile(MultipartFile file) {
        OSS ossClient = null;
        try {
            // 2305a-images.oss-cn-beijing.aliyuncs.com  oss-cn-beijing.aliyuncs.com
            // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//            String endpoint = "https://oss-cn-beijing.aliyuncs.com";
            // 填写Bucket名称，例如examplebucket。
//            String bucketName = bucket;
            // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
            // 生成一个文件名
            String fileName = file.getOriginalFilename();
            // xxx.JPG   xxxx.jpg   HTML  html  TXT  txt
            String filePostfix = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + filePostfix;
            // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
            /***
             * public OSS build(String endpoint, String accessKeyId, String secretAccessKey) {
             *         return new OSSClient(endpoint, getDefaultCredentialProvider(accessKeyId, secretAccessKey), getClientConfiguration());
             *     }
             */
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET);

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, dir + fileName, file.getInputStream());
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            //  https://2305a-images.oss-cn-beijing.aliyuncs.com/data/f646c44319774a37a36f18bfb5e86ff4.png
            return prefix + dir + fileName;
        } catch (Exception oe) {

        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

}
