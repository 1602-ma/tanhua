package com.feng.mannage.controller;

import com.feng.domain.po.UserInfo;
import com.feng.domain.vo.PageResult;
import com.feng.mannage.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author f
 * @date 2023/5/11 21:54
 */
@RestController
@RequestMapping("/manage")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户管理页面分页查询
     * @param page      page
     * @param pagesize  pageSize
     * @return          page
     */
    @GetMapping("/users")
    public ResponseEntity findPage(@RequestParam(value = "page", defaultValue = "1") Long page
            , @RequestParam(value = "pagesize", defaultValue = "10") Long pagesize) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("page", page);
        paramMap.put("pagesize", pagesize);
        PageResult<UserInfo> result = userService.findPage(paramMap);
        return ResponseEntity.ok(result);
    }

    /**
     * 用户详情
     * @param userId userId
     * @return       res
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity findUserDetail(@PathVariable(value = "userId") long userId) {
        UserInfo userInfo = userService.findById(userId);
        return ResponseEntity.ok(userInfo);
    }

    /**
     * 获取当前用户所有的视频列表
     * @param page      page
     * @param pageSize  pageSize
     * @param uid       uid
     * @return          res
     */
    @GetMapping("videos")
    public ResponseEntity findAllVideos(@RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pageSize
            , @RequestParam(required = false) Long uid) {
        return userService.findAllVideos(page, pageSize, uid);
    }

    /**
     * 获取当前用户的所有动态分页列表
     * @param page      page
     * @param pageSize  pageSize
     * @param uid       uid
     * @param state     state
     * @return          res
     */
    @GetMapping("/messages")
    public ResponseEntity findAllMovements(@RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int pageSize
            , @RequestParam(required = false) long uid
            , @RequestParam(required = false) String state) {
        return userService.findAllMovements(page, pageSize, uid, 0);
    }

    /**
     * 根据id查询，动态详情
     */
    @GetMapping("/messages/{publishId}")
    public ResponseEntity findMovementById(@PathVariable("publishId") String publishId) {
        return userService.findMovementById(publishId);
    }

    /**
     * 查看动态的评论列表
     */
    @GetMapping("/messages/comments")
    public ResponseEntity findAllComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pagesize,
            String messageID) {
        return userService.findAllComments(page,pagesize,messageID);
    }
}
