package com.feng.test;

import com.feng.commons.templates.FaceTemplate;
import com.feng.server.TanhuaServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author f
 * @date 2023/5/1 23:44
 */
@SpringBootTest(classes = TanhuaServerApplication.class)
@RunWith(SpringRunner.class)
public class FaceTest {

    @Resource
    private FaceTemplate faceTemplate;

    @Test
    public void testFace() throws IOException {
        File file = new File("H:\\1.jpg");
        boolean detect = faceTemplate.detect(Files.readAllBytes(file.toPath()));
        System.out.println("================================result:   " + detect);
    }
}
