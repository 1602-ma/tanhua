package com.feng.mannage.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.feng.mannage.domain.Admin;
import com.feng.mannage.interceptor.AdminHolder;
import com.feng.mannage.service.AdminService;
import com.feng.mannage.vo.AdminVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author f
 * @date 2023/5/10 21:41
 */
@RestController
@RequestMapping("/system/users")
@Slf4j
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 后台管理系统：图片验证码生成
     * @param uuid      uuid
     * @param request   request
     * @param response  response
     */
    @GetMapping("/verification")
    public void showValidateCodePic(String uuid, HttpServletRequest request, HttpServletResponse response) {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(299, 97);
        String code = lineCaptcha.getCode();
        log.info("uuid={},code={}", uuid, code);
        adminService.saveCode(uuid, code);
        try {
            lineCaptcha.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("fail==========================");
        }
    }

    /**
     * login
     * @param map map
     * @return    res
     */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> map) {
        return adminService.login(map);
    }

    /**
     * user detail
     * @return user
     */
    @PostMapping("/profile")
    public ResponseEntity profile() {
        Admin admin = AdminHolder.getAdmin();
        AdminVo vo = new AdminVo();
        BeanUtils.copyProperties(admin, vo);;
        return ResponseEntity.ok(vo);
    }

    /**
     * logout
     * @param token token
     * @return      out
     */
    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token) {
        token.replace("Bearer", "");
        return adminService.logout(token);
    }
}
