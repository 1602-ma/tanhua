package com.feng.server.controller;

import com.feng.domain.po.User;
import com.feng.domain.po.UserInfo;
import com.feng.domain.vo.ErrorResult;
import com.feng.domain.vo.UserInfoVo;
import com.feng.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/2 11:01
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 个人信息
     * @param userInfoVo userInfoVo
     * @param token      token
     * @return           res
     */
    @PostMapping("/loginReginfo")
    public ResponseEntity loginReginfo(@RequestBody UserInfoVo userInfoVo, @RequestHeader("Authorization") String token) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoVo, userInfo);
        userService.saveUserInfo(userInfo, token);
        return ResponseEntity.ok(null);
    }

    /**
     * 上传用户头像
     * @param headPhoto photo
     * @param token     token
     * @return          res
     */
    @PostMapping("/loginReginfo/head")
    public ResponseEntity uploadAvatar(MultipartFile headPhoto, @RequestHeader("Authorization") String token) {
        userService.updateUserAvatar(headPhoto, token);
        return ResponseEntity.ok(null);
    }
}
