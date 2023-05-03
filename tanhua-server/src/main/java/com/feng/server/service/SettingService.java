package com.feng.server.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.feng.commons.TanHuaException;
import com.feng.commons.constant.RedisKeyConst;
import com.feng.commons.templates.SmsTemplate;
import com.feng.domain.po.Question;
import com.feng.domain.po.Settings;
import com.feng.domain.po.User;
import com.feng.domain.po.UserInfo;
import com.feng.domain.vo.ErrorResult;
import com.feng.domain.vo.PageResult;
import com.feng.domain.vo.SettingsVo;
import com.feng.dubbo.api.BlackListApi;
import com.feng.dubbo.api.QuestionApi;
import com.feng.dubbo.api.SettingApi;
import com.feng.dubbo.api.UserApi;
import com.feng.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.http.cookie.SM;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author f
 * @date 2023/5/3 10:47
 */
@Service
@Slf4j
public class SettingService {

    @Reference
    private SettingApi settingApi;

    @Reference
    private QuestionApi questionApi;

    @Reference
    private BlackListApi blackListApi;

    @Reference
    private UserApi userApi;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SmsTemplate smsTemplate;

    /**
     * 查询用户通用设置
     * @return vo
     */
    public SettingsVo querySettings() {
        User user = UserHolder.getUser();
        SettingsVo vo = new SettingsVo();

        Question question = questionApi.findByUserId(user.getId());
        if (null != question) {
            vo.setStrangerQuestion(question.getTxt());
        }

        Settings settings = settingApi.findByUserId(user.getId());
        if (null != settings) {
            BeanUtils.copyProperties(settings, vo);
        }

        vo.setPhone(user.getMobile());
        return vo;
    }

    /**
     * 保存或更新 通知
     * @param like   like
     * @param pinlun pinlun
     * @param gongao gonggao
     */
    public void updateNotification(boolean like, boolean pinlun, boolean gongao) {
        Settings settings = settingApi.findByUserId(UserHolder.getUserId());
        if (null == settings) {
            settings = new Settings();
            settings.setUserId(UserHolder.getUserId());
            settings.setLikeNotification(like);
            settings.setGonggaoNotification(gongao);
            settings.setPinglunNotification(pinlun);
            settingApi.save(settings);
        }else {
            settings.setLikeNotification(like);
            settings.setGonggaoNotification(gongao);
            settings.setPinglunNotification(pinlun);
            settingApi.update(settings);
        }
    }

    /**
     * 分页查询当前用户的黑名单
     * @param page     page
     * @param pagesize pageSize
     * @return         res
     */
    public ResponseEntity findBlackList(int page, int pagesize) {
        Long userId = UserHolder.getUserId();
        PageResult<UserInfo> list = blackListApi.findBlackList(page, pagesize, userId);
        return ResponseEntity.ok(list);
    }

    /**
     * 移除黑名单
     * @param deleteUserId userId
     * @return             res
     */
    public ResponseEntity deleteBlackList(Long deleteUserId) {
        Long userId = UserHolder.getUserId();
        blackListApi.delete(userId, deleteUserId);
        return ResponseEntity.ok(null);
    }

    /**
     * 保存或者更新用户提问问题
     * @param content content
     * @return        res
     */
    public ResponseEntity saveQuestion(String content) {
        Question question = questionApi.findByUserId(UserHolder.getUserId());
        if (null == question) {
            question = new Question();
            question.setUserId(UserHolder.getUserId());
            question.setTxt(content);
            questionApi.save(question);
        }else {
            question.setTxt(content);
            questionApi.update(question);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 修改手机号：发送验证码
     */
    public void sendVerificationCode() {
        String mobile = UserHolder.getUser().getMobile();

        String key = RedisKeyConst.CHANGE_MOBILE_VALIDATE_CODE + mobile;
        String codeInRedis = (String) redisTemplate.opsForValue().get(key);
        log.info("=========================redis中的验证码，修改手机号：{}，{}", codeInRedis, mobile);

        if (StringUtils.isNotEmpty(codeInRedis)) {
            throw new TanHuaException(ErrorResult.duplicate());
        }

//        String validateCode = RandomStringUtils.randomNumeric(6);
        String validateCode = "888888";
        log.info("=====================发送的手机号，验证码：{}, {}", mobile, validateCode);
//        Map<String, String> smsResult = smsTemplate.sendValidateCode(mobile, validateCode);
//        if (null != smsResult) {
//            throw new TanHuaException(ErrorResult.fail());
//        }
        log.info("======================== 验证码存入redis");
        redisTemplate.opsForValue().set(key, validateCode, 5, TimeUnit.DAYS);
    }

    /**
     * 修改手机号：校验验证码
     * @param verificationCode code
     * @return                 boolean
     */
    public boolean checkValidateCode(String verificationCode) {
        User user = UserHolder.getUser();
        String mobile = user.getMobile();
        String key = RedisKeyConst.CHANGE_MOBILE_VALIDATE_CODE + mobile;
        String codeInRedis = (String)redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);

        if (StringUtils.isEmpty(codeInRedis)) {
            throw new TanHuaException(ErrorResult.loginError());
        }
        if (!codeInRedis.equals(verificationCode)) {
            return false;
        }
        return true;
    }

    /**
     * 修改手机号：保存
     * @param phone phone
     * @param token token
     */
    public void changeMobile(String phone, String token) {
        Long userId = UserHolder.getUserId();
        userApi.updateMobile(userId, phone);
        log.info("修改手机号码成功，old:{},new:{}", UserHolder.getUser().getMobile(), phone);

        // 让用户重新登录
        String key = RedisKeyConst.TOKEN + token;
        redisTemplate.delete(key);
    }
}
