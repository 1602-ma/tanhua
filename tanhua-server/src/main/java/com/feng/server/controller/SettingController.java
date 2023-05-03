package com.feng.server.controller;

import com.feng.domain.vo.SettingsVo;
import com.feng.server.service.SettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author f
 * @date 2023/5/3 10:47
 */
@RestController
@RequestMapping("/users")
public class SettingController {

    @Resource
    private SettingService settingService;

    /**
     * 获取用户通用配置
     * @return res
     */
    @GetMapping("/settings")
    public ResponseEntity querySettings() {
        SettingsVo vo = settingService.querySettings();
        return ResponseEntity.ok(vo);
    }

    /**
     * 通知设置
     * @param map map
     * @return    res
     */
    @PostMapping("/notifications/setting")
    public ResponseEntity updateNotification(@RequestBody Map map) {
        boolean like = (Boolean)map.get("likeNotification");
        boolean pinlun = (Boolean)map.get("pinglunNotification");
        boolean gongao = (Boolean)map.get("gonggaoNotification");

        settingService.updateNotification(like, pinlun, gongao);
        return ResponseEntity.ok(null);
    }

    /**
     * 分页查询黑名单列表
     * @param page     page
     * @param pagesize pageSize
     * @return         list
     */
    @GetMapping("/blacklist")
    public ResponseEntity findBlackList(@RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pagesize) {
        return settingService.findBlackList(page, pagesize);
    }

    /**
     * 移除黑名单
     * @param deleteUserId userId
     * @return             res
     */
    @DeleteMapping("/blacklist/{uid}")
    public ResponseEntity deleteBlackList(@PathVariable("uid") Long deleteUserId) {
        return settingService.deleteBlackList(deleteUserId);
    }

    /**
     * 保存或者更新陌生人问题
     * @param map map
     * @return    res
     */
    @PostMapping("/questions")
    public ResponseEntity saveQuestion(@RequestBody Map map) {
        String content = (String)map.get("content");
        return settingService.saveQuestion(content);
    }

    /**
     * 修改手机号：发送验证码
     * @return res
     */
    @PostMapping("/phone/sendVerificationCode")
    public ResponseEntity sendVerificationCode() {
        settingService.sendVerificationCode();
        return ResponseEntity.ok(null);
    }

    /**
     * 修改手机号：校验验证码
     * @param map map
     * @return    res
     */
    @PostMapping("/phone/checkVerificationCode")
    public ResponseEntity checkVerificationCode(@RequestBody Map<String, Object> map) {
        boolean flag = settingService.checkValidateCode((String) map.get("verificationCode"));
        Map<String, Object> result = new HashMap<>();
        result.put("verification", flag);
        return ResponseEntity.ok(result);
    }

    /**
     * 修改手机号：保存
     * @param param param
     * @param token token
     * @return      res
     */
    @PostMapping("/phone")
    public ResponseEntity changeMobile(@RequestBody Map<String, String> param, @RequestHeader("Authorization") String token) {
        settingService.changeMobile(param.get("phone"), token);
        return ResponseEntity.ok(null);
    }
}
