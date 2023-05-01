package com.feng.server.utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author f
 * @date 2023/5/1 14:55
 */
@Component
public class JwtUtils {

    @Value("${tanhua.secret}")
    private String secret;

    public String createJWT(String phone, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("mobile", phone);
        claims.put("id", userId);
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, secret);
        return builder.compact();
    }
}
