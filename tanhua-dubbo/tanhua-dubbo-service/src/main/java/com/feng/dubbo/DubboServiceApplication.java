package com.feng.dubbo;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @author f
 * @date 2023/4/30 11:40
 */
@SpringBootApplication
@MapperScan(value = "com.feng.dubbo.mapper")
public class DubboServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboServiceApplication.class, args);
        System.out.println("==========================================================================");
        System.out.println("                              start success                               ");
        System.out.println("==========================================================================");

    }
}
