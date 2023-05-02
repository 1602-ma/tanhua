package com.feng.commons;

import com.feng.commons.properties.FaceProperties;
import com.feng.commons.properties.OssProperties;
import com.feng.commons.properties.SmsProperties;
import com.feng.commons.templates.FaceTemplate;
import com.feng.commons.templates.OssTemplate;
import com.feng.commons.templates.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author f
 * @date 2023/4/30 15:42
 */
@Configuration
@EnableConfigurationProperties({SmsProperties.class
        , OssProperties.class
        , FaceProperties.class})
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
}
