package com.feng.mannage.service;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.mannage.domain.Admin;
import com.feng.mannage.exception.BusinessException;
import com.feng.mannage.mapper.AdminMapper;
import com.feng.mannage.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author f
 * @date 2023/5/10 21:41
 */
@Service
@Slf4j
public class AdminService extends ServiceImpl<AdminMapper, Admin> {

    private static final String CACHE_KEY_CAP_PREFIX = "MANAGE_CAP_";
    public static final String CACHE_KEY_TOKEN_PREFIX="MANAGE_TOKEN_";


    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private JwtUtils jwtUtils;

    /**
     * 保存生成的验证码
     * @param uuid uuid
     * @param code code
     */
    public void saveCode(String uuid, String code) {
        String key = CACHE_KEY_CAP_PREFIX + uuid;
        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(10));
    }

    /**
     * 获取登陆用户信息
     * @return
     */
    public Admin getByToken(String authorization) {
        String token = authorization.replaceFirst("Bearer ","");
        String tokenKey = CACHE_KEY_TOKEN_PREFIX + token;
        String adminString = (String) redisTemplate.opsForValue().get(tokenKey);
        Admin admin = null;
        if(StringUtils.isNotEmpty(adminString)) {
            admin = JSON.parseObject(adminString, Admin.class);
            // 延长有效期 30分钟
            redisTemplate.expire(tokenKey,30, TimeUnit.MINUTES);
        }
        return admin;
    }

    /**
     * login
     * @param map map
     * @return    res
     */
    public ResponseEntity login(Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String verificationCode = map.get("verificationCode");
        String uuid = map.get("uuid");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BusinessException("用户名或密码为空");
        }

        if (StringUtils.isEmpty(verificationCode) || StringUtils.isEmpty(uuid)) {
            throw new BusinessException("验证码为空");
        }

        String key = CACHE_KEY_CAP_PREFIX + uuid;
        String value = (String)redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(value) || !value.equals(verificationCode)) {
            throw new BusinessException("验证码错误");
        }

        redisTemplate.delete(key);

        Admin admin = query().eq("username", username).one();
        if (null == admin) {
            throw new BusinessException("用户名错误");
        }

        if (!admin.getPassword().equals(SecureUtil.md5(password))) {
            throw new BusinessException("密码错误");
        }

        String token = jwtUtils.createJWT(admin.getUsername(), admin.getId());
        String adminStr = JSON.toJSONString(admin);
        redisTemplate.opsForValue().set(CACHE_KEY_TOKEN_PREFIX + token, adminStr, Duration.ofHours(1));

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        return ResponseEntity.ok(result);
    }

    /**
     * logout
     * @param token token
     * @return      out
     */
    public ResponseEntity logout(String token) {
        String key = CACHE_KEY_TOKEN_PREFIX + token;
        redisTemplate.delete(key);
        return ResponseEntity.ok(null);
    }
}
