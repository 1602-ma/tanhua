package com.feng.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @author f
 * @date 2023/4/30 11:53
 */
@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
public class TanhuaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TanhuaServerApplication.class, args);
        System.out.println("==========================================================================");
        System.out.println("                              start success                               ");
        System.out.println("==========================================================================");
    }
}
