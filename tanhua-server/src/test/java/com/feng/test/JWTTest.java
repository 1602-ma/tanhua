package com.feng.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author f
 * @date 2023/5/1 14:38
 */
public class JWTTest {

    @Test
    public void testJWT() {
        String secret = "test";

        Map<String, Object> claims = new HashMap<>();
        claims.put("mobile", "11111111111");
        claims.put("id", "2");

        // 生成token
        String token = Jwts.builder()
                // 设置响应数据体
                .setClaims(claims)
                // 设置加密方法和加密盐
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        System.out.println(token);

        // 通过token解析数据
        Map<String, Object> boday = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        System.out.println(boday);
    }

    /**
     * 生成jwt使用的密钥
     */
    @Test
    public void createSecret() {
        // 098f6bcd4621d373cade4e832627b4f6
        System.out.println(DigestUtils.md5Hex("test"));
    }
}
