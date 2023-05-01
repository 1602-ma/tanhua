package com.feng.commons.templates;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.feng.commons.properties.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author f
 * @date 2023/4/30 12:09
 */
@Slf4j
public class SmsTemplate {

    /** SMSRESPONSE_CODE SMSRESPONSE_MESSAGE 用来返回给调用者，短信发送结果的key */
    public static final String SMSRESPONSE_CODE="Code";
    public static final String SMSRESPONSE_MESSAGE="Message";
    private SmsProperties smsProperties;
    private IAcsClient acsClient;

    public SmsTemplate(SmsProperties smsProperties){
        this.smsProperties = smsProperties;
    }

    public void init() {
        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化ascClient需要的几个参数
        // 短信API产品名称（短信产品名固定，无需修改）
        final String product = "Dysmsapi";
        // 短信API产品域名（接口地址固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsProperties.getAccessKeyId(), smsProperties.getAccessKeySecret());
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            acsClient = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            log.error("初始化阿里云短信失败",e);
        }

    }

    /**
     * 发送验证码
     * @param phoneNumber   phone
     * @param validateCode  validateCode
     * @return              map
     */
    public Map<String, String> sendValidateCode(String phoneNumber, String validateCode) {
        return sendSms(smsProperties.getValidateCodeTemplateCode(), phoneNumber, validateCode);
    }

    /**
     * 发送短信
     * @param templateCode templateCode
     * @param phoneNumber  phoneNumber
     * @param param        param
     * @return             map
     */
    public Map<String, String> sendSms(String templateCode, String phoneNumber, String param) {
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(phoneNumber);
        request.setSignName(smsProperties.getSignName());
        request.setTemplateCode(templateCode);
        request.setTemplateParam(String.format("{\"%s\":\"%s\"}",  smsProperties.getParameterName(), param));

        Map<String, String> result = new HashMap<>();
        try {
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if ("ok".equals(sendSmsResponse.getCode())) {
                return null;
            }
            result.put(SmsTemplate.SMSRESPONSE_CODE, sendSmsResponse.getCode());
            result.put(SmsTemplate.SMSRESPONSE_MESSAGE, sendSmsResponse.getMessage());
        } catch (ClientException e) {
            log.error("发送短信失败！", e);
            result.put(SmsTemplate.SMSRESPONSE_CODE, "FAIL");
            return null;
        }
        return result;
    }
}
