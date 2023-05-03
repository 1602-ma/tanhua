package com.feng.server.controller;

import com.feng.domain.po.User;
import com.feng.domain.vo.ErrorResult;
import com.feng.domain.vo.UserInfoVo;
import com.feng.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.print.DocFlavor;

/**
 * @author f
 * @date 2023/5/2 11:59
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserInfoController {

    @Resource
    private UserService userService;

    /**
     * 获取用户信息
     * @param userID    userId
     * @param huanxinId huanxinId
     * @param token     token
     * @return          userInto
     */
    @GetMapping
    public ResponseEntity getUserInfo(Long userID, Long huanxinId, @RequestHeader("Authorization") String token) {
        log.info("=============================================, getUserInfo");
        Long userId = huanxinId;
        if(null == userId) {
            userId = userID;
        }

        if (null == userId) {
            User user = userService.getUserByToken(token);
            if (null == user) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResult.error("000006", "网络错误，请稍后再试"));
            }
            userId = user.getId();
        }

        UserInfoVo userInfoVo = userService.findUserInfoById(userId);
        log.info("====================================, userInfo:{}", userInfoVo.toString());
        return ResponseEntity.ok(userInfoVo);
    }

    /**
     * 更新用户基本信息
     * @param vo    vo
     * @param token token
     * @return      res
     */
    @PutMapping
    public ResponseEntity updateUserInfo(@RequestBody UserInfoVo vo, @RequestHeader("Authorization") String token) {
        userService.updateUserInfo(vo, token);
        return ResponseEntity.ok(null);
    }

    /**
     * 更新用户头像
     * @param token     token
     * @param headPhoto photo
     * @return          res
     */
    @PostMapping("/header")
    public ResponseEntity header(@RequestHeader("Authorization") String token, MultipartFile headPhoto) {
        userService.header(token, headPhoto);
        return ResponseEntity.ok(null);
    }
}
