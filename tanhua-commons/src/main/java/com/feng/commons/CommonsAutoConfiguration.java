package com.feng.commons;

import com.feng.commons.properties.SmsProperties;
import com.feng.commons.templates.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author f
 * @date 2023/4/30 15:42
 */
@Configuration
@EnableConfigurationProperties({SmsProperties.class})
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
}
