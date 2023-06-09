package com.feng.commons;

import com.feng.commons.properties.*;
import com.feng.commons.templates.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author f
 * @date 2023/4/30 15:42
 */
@Configuration
@EnableConfigurationProperties({SmsProperties.class
        , OssProperties.class
        , FaceProperties.class
        , HuanXinProperties.class
        , HuaWeiUGCProperties.class})
public class CommonsAutoConfiguration {

    /**
     * sms
     * @param smsProperties smsProperties
     * @return              smsTemplate
     */
    @Bean
    public SmsTemplate smsTemplate(SmsProperties smsProperties) {
        SmsTemplate smsTemplate = new SmsTemplate(smsProperties);
        smsTemplate.init();
        return smsTemplate;
    }

    /**
     * oss
     * @param ossProperties ossProperties
     * @return              ossTemplate
     */
    @Bean
    public OssTemplate ossTemplate(OssProperties ossProperties) {
        OssTemplate ossTemplate = new OssTemplate(ossProperties);
        return ossTemplate;
    }

    /**
     * face
     * @param faceProperties faceProperties
     * @return               faceTemplate
     */
    @Bean
    public FaceTemplate faceTemplate(FaceProperties faceProperties) {
        return new FaceTemplate(faceProperties);
    }

    @Bean
    public HuanXinTemplate huanXinTemplate(HuanXinProperties huanXinProperties){
        return new HuanXinTemplate(huanXinProperties);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

    @Bean
    public HuaWeiUGCTemplate huaWeiUGCTemplate(HuaWeiUGCProperties properties) {
        return new HuaWeiUGCTemplate(properties);
    }

}
