package com.feng.commons.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author f
 * @date 2023/5/1 23:32
 */
@Data
@ConfigurationProperties(prefix = "tanhua.face")
public class FaceProperties {

    private String appId;
    private String apiKey;
    private String secretKey;
}
