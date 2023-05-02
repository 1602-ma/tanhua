package com.feng.commons.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author f
 * @date 2023/5/1 23:10
 */
@Data
@ConfigurationProperties(prefix = "tanhua.oss")
public class OssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String url;
}
