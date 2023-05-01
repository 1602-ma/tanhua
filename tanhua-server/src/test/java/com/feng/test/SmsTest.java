package com.feng.test;

import com.feng.commons.templates.SmsTemplate;
import com.feng.server.TanhuaServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author f
 * @date 2023/4/30 15:47
 */
@SpringBootTest(classes = TanhuaServerApplication.class)
@RunWith(SpringRunner.class)
public class SmsTest {

    @Resource
    private SmsTemplate smsTemplate;

    @Test
    public void testSms() {
        Map<String, String> result = smsTemplate.sendValidateCode("178", "666666");
        System.out.println(result);
    }
}
