package com.aisile.utils.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@PropertySource("classpath:aliyun.properties")
public class OSSClientUtil {

    @Value("${OSS_ACCESS_KEY_ID}")
    private String OSS_ACCESS_KEY_ID;
    @Value("${OSS_ACCESS_KEY_SECRET}")
    private String OSS_ACCESS_KEY_SECRET;
    @Value("${prefix}")
    private String prefix;
    @Value("${bucket}")
    private String bucket;
    @Value("${dir}")
    private String dir;
    @Value("${endpoint}")
    private String endpoint;


    public String uploadFile(MultipartFile file) {
        System.out.println(OSS_ACCESS_KEY_ID);
        System.out.println(prefix);
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
            System.out.println(result);
            //  https://2305a-images.oss-cn-beijing.aliyuncs.com/data/f646c44319774a37a36f18bfb5e86ff4.png
            return prefix + dir + fileName;
        } catch (Exception oe) {
            oe.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

}
