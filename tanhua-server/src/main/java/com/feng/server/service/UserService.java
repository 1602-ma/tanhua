package com.feng.server.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import com.feng.commons.TanHuaException;
import com.feng.commons.templates.FaceTemplate;
import com.feng.commons.templates.OssTemplate;
import com.feng.commons.templates.SmsTemplate;
import com.feng.domain.po.User;
import com.feng.domain.po.UserInfo;
import com.feng.domain.vo.ErrorResult;
import com.feng.domain.vo.UserInfoVo;
import com.feng.dubbo.api.UserApi;
import com.feng.dubbo.api.UserInfoApi;
import com.feng.server.utils.GetAgeUtil;
import com.feng.server.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author f
 * @date 2023/4/30 11:45
 */
@Service
@Slf4j
public class UserService {

    @Reference
    private UserApi userApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Resource
    private SmsTemplate smsTemplate;

    @Resource
    private FaceTemplate faceTemplate;

    @Resource
    private OssTemplate ossTemplate;

    @Value("${tanhua.redisValidateCodeKeyPrefix}")
    private String redisValidateCodeKeyPrefix;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private JwtUtils jwtUtils;

    public ResponseEntity findByMobile(String mobile) {
        User user = userApi.findByMobile(mobile);
        return ResponseEntity.ok(user);
    }

    /**
     * 新增用户
     * @param mobile    mobile
     * @param password  password
     * @return          res
     */
    public ResponseEntity saveUser(String mobile, String password) {
        User user = new User();
        user.setMobile(mobile);
        user.setPassword(password);
        userApi.save(user);
        return ResponseEntity.ok("success");
    }

    /**
     * 发送验证码
     * @param phone phone
     */
    public void sendValidateCode(String phone) {
        // 1.redis中存入验证码的key
        String key = redisValidateCodeKeyPrefix + phone;
        // 2. redis中的验证码
        String codeInRedis = redisTemplate.opsForValue().get(key);
        // 3.上次验证码未失效
        if (StringUtils.isNotEmpty(codeInRedis)) {
            throw new TanHuaException(ErrorResult.duplicate());
        }
        // 4.生成验证码
//        String validateCode = RandomStringUtils.randomNumeric(6);
        String validateCode = "999999";
        log.info("生成的验证码：{}, {}", phone, validateCode);
        // 5.发送验证码
//        Map<String, String> smsRs = smsTemplate.sendValidateCode(phone, validateCode);
//        if (null != smsRs) {
//            throw new TanHuaException(ErrorResult.fail());
//        }
        // 6.将验证码存入redis
        log.info("将验证码存入redis");
        redisTemplate.opsForValue().set(key, validateCode, 5, TimeUnit.DAYS);
    }

    /**
     * 登录或注册
     * @param phone            phone
     * @param verificationCode code
     * @return                 map
     */
    public Map<String, Object> loginVerification(String phone, String verificationCode) {
        String key = redisValidateCodeKeyPrefix + phone;
        String codeInRedis = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        log.debug("==========================校验验证码：{}, {}, {}", phone, codeInRedis, verificationCode);
        if (StringUtils.isEmpty(codeInRedis)) {
            throw new TanHuaException(ErrorResult.loginError());
        }
        if (!codeInRedis.equals(verificationCode)) {
            throw new TanHuaException(ErrorResult.validateCodeError());
        }
        User user = userApi.findByMobile(phone);
        boolean isNew = false;
        if (null == user) {
            user = new User();
            user.setMobile(phone);
            user.setPassword(DigestUtils.md5Hex(phone.substring(phone.length() - 6)));
            log.info("================================新增用户，{}", phone);
            Long userId = userApi.save(user);
            user.setId(userId);
            isNew = true;
        }
        String token = jwtUtils.createJWT(phone, user.getId());
        String userStr = JSON.toJSONString(user);
        redisTemplate.opsForValue().set("TOKEN_" + token, userStr, 5, TimeUnit.DAYS);
        log.debug("================================ 签发token:{}", token);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isNew", isNew);
        resultMap.put("token", token);
        return resultMap;
    }

    /**
     * 通过token获取登录用户信息
     * @param token token
     * @return      user
     */
    public User getUserByToken(String token) {
        String key = "TOKEN_" + token;
        String userJsonStr = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(userJsonStr)) {
            return null;
        }

        redisTemplate.expire(key, 1, TimeUnit.DAYS);
        User user = JSON.parseObject(userJsonStr, User.class);
        return user;
    }

    /**
     * 完善用户信息
     * @param userInfo userInfo
     * @param token    token
     */
    public void saveUserInfo(UserInfo userInfo, String token) {
        User user = getUserByToken(token);
        if (null == user) {
            throw new  TanHuaException("登录超时，请重新登录");
        }
        userInfo.setId(user.getId());
        userInfoApi.save(userInfo);
    }

    /**
     * 上传用户头像处理
     * @param headPhoto photo
     * @param token     token
     */
    public void updateUserAvatar(MultipartFile headPhoto, String token) {
        User user = getUserByToken(token);
        if (null == user) {
            throw new TanHuaException("登录超时，请重新登录");
        }
        try {
            String filename = headPhoto.getOriginalFilename();
            byte[] bytes = headPhoto.getBytes();
//            if (！faceTemplate.detect(bytes)) {
//                throw new TanHuaException("未检测到人像，请重新上传");
//            }
//            String avatar = ossTemplate.upload(filename, headPhoto.getInputStream());
            String avatar = "doc\\images\\2.jpg";
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setAvatar(avatar);

            userInfoApi.update(userInfo);
        } catch (IOException e) {
            log.error("上传头像失败");
            throw new TanHuaException("上传头像失败，请稍后再试");
        }
    }

    /**
     * 根据id查询用户基本信息
     * @param id id
     * @return   vo
     */
    public UserInfoVo findUserInfoById(Long id) {
        UserInfo userInfo = userInfoApi.findById(id);
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, vo);

        if (null != userInfo.getAge()) {
            vo.setAge(String.valueOf(userInfo.getAge()));
        }
        return vo;
    }

    /**
     * 更新用户基本信息
     * @param vo    vo
     * @param token token
     */
    public void updateUserInfo(UserInfoVo vo, String token) {
        User user = getUserByToken(token);
        if (null == user) {
            throw new TanHuaException("登录超时，请重新登录");
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(vo, userInfo);

        int age = GetAgeUtil.getAge(userInfo.getBirthday());
        userInfo.setAge(age);
        userInfo.setId(user.getId());
        userInfoApi.update(userInfo);
        log.info("=============================== 已更新用户信息");
    }

    /**
     * 更新用户头像
     * @param token      token
     * @param headPhoto  photo
     */
    public void header(String token, MultipartFile headPhoto) {
        try {
            String userStr = redisTemplate.opsForValue().get("TOKEN_" + token);
            if (StringUtils.isEmpty(userStr)) {
                throw new TanHuaException("登录超时，请重新登录");
            }

            User user = JSON.parseObject(userStr, User.class);
            Long userId = user.getId();

            UserInfo userInfo = userInfoApi.findById(userId);
            String oldAvatar = userInfo.getAvatar();
//            boolean detect = faceTemplate.detect(headPhoto.getBytes());
            String filename = headPhoto.getOriginalFilename();
//            String avatar = ossTemplate.upload(filename, headPhoto.getInputStream());
            String avatar = "1.jpg";

            userInfo.setAvatar(avatar);
            userInfoApi.update(userInfo);
            log.info("=============================，已更新用户头像");
//            ossTemplate.deleteFile(oldAvatar);
        } catch (Exception e) {
            log.error("更新用户头像失败");
            throw new TanHuaException("更新用户头像失败，请稍后再试");
        }
    }
}
