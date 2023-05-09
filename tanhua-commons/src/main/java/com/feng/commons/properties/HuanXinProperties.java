package com.feng.commons.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author f
 * @date 2023/5/9 23:04
 */
@Configuration
@ConfigurationProperties(prefix = "tanhua.huanxin")
@Data
public class HuanXinProperties {

    private String url;
    private String orgName;
    private String appName;
    private String clientId;
    private String clientSecret;

    public String getHuanXinUrl() {
        return this.url
                + this.orgName + "/"
                + this.appName;
    }
}
