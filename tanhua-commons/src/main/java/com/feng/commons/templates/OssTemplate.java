package com.feng.commons.templates;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.feng.commons.properties.OssProperties;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author f
 * @date 2023/5/1 23:12
 */
public class OssTemplate {

    private OssProperties ossProperties;

    public OssTemplate(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    /**
     * 上传文件 oss
     * @param filename filename
     * @param is       is
     * @return         url
     */
    public String upload(String filename, InputStream is) {
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String suffix = filename.substring(filename.lastIndexOf("."));
        String now = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String name = "images/" + now + "/" + UUID.randomUUID().toString() + suffix;

        ossClient.putObject(ossProperties.getBucketName(), name, is);

        ossClient.shutdown();
        return ossProperties.getUrl() + "/" + name;
    }
}
