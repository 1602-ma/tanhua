package com.feng.test;

import com.feng.commons.TanHuaException;
import com.feng.commons.templates.OssTemplate;
import com.feng.server.TanhuaServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author f
 * @date 2023/5/1 23:27
 */
@SpringBootTest(classes = TanhuaServerApplication.class)
@RunWith(SpringRunner.class)
public class OssTest {

    @Resource
    private OssTemplate ossTemplate;

    @Test
    public void testOss() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("d:\\2.jpg");
        String url = ossTemplate.upload("2.jpg", fis);
        System.out.println(url);
    }
}
