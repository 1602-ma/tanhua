package com.feng.test;

import com.feng.server.TanhuaServerApplication;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @author f
 * @date 2023/5/9 21:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TanhuaServerApplication.class)
public class FastDFSTest {

    @Resource
    private FastFileStorageClient client;

    @Resource
    private FdfsWebServer fdfsWebServer;

    @Test
    public void testUploadFile() throws IOException {
        File file = new File("2.jpg");
        StorePath storePath = client.uploadFile(FileUtils.openInputStream(file), file.length(), "jpg", null);
        System.out.println(storePath.getFullPath());
        System.out.println(storePath.getPath());

        String url = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
        System.out.println(url);
    }
}
