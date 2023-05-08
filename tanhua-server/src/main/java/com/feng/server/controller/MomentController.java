package com.feng.server.controller;

import com.feng.domain.ro.PublishVo;
import com.feng.domain.vo.MomentVo;
import com.feng.domain.vo.PageResult;
import com.feng.server.interceptor.UserHolder;
import com.feng.server.service.CommentService;
import com.feng.server.service.MovementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/4 21:22
 */
@RestController
@RequestMapping("/movements")
@Slf4j
public class MomentController {

    @Resource
    private MovementService movementService;

    @Resource
    private CommentService commentService;

    /**
     * 发布动态
     * @param publishVo publish
     * @param image     image
     * @return          res
     */
    @PostMapping
    public ResponseEntity postMoment(PublishVo publishVo, MultipartFile[] image) {
        movementService.save(publishVo, image);
        return ResponseEntity.ok(null);
    }

    /**
     * 查询好友动态
     * @param page      page
     * @param pageSize  pageSize
     * @return          list
     */
    @GetMapping
    public ResponseEntity queryFriendPublishList(@RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10")int pageSize) {
        log.info("==================================,查询好友动态，{}", UserHolder.getUserId());
        page = page < 1 ? 1 : page;
        PageResult<MomentVo> pageResult = movementService.queryFriendPublishList(page, pageSize);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 查询推荐动态
     * @param page      page
     * @param pageSize  pageSize
     * @return          list
     */
    @GetMapping("/recommend")
    public ResponseEntity queryRecommendPublishList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        log.info("===========================查询推荐动态");
        page = page < 1 ? 1 : page;
        PageResult<MomentVo> pageResult = movementService.queryRecommendPublishList(page, pageSize);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 我的动态
     * @param page      page
     * @param pageSize  pageSize
     * @return          list
     */
    @GetMapping("/all")
    public ResponseEntity queryMyAlbum(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize, Long userId) {
        page = page < 1 ? 1 : page;
        PageResult<MomentVo> pageResult = movementService.queryMyAlbum(page, pageSize,userId);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 点赞
     * @param publishId publishId
     * @return          long
     */
    @GetMapping("/{id}/like")
    public ResponseEntity<Long> like(@PathVariable("id") String publishId) {
        Long total = commentService.like(publishId);
        return ResponseEntity.ok(total);
    }

    /**
     * 取消点赞
     * @param publishId     publishId
     * @return              long
     */
    @GetMapping("/{id}/dislike")
    public ResponseEntity<Long> unlike(@PathVariable("id") String publishId) {
        Long total = commentService.unlike(publishId);
        return ResponseEntity.ok(total);
    }

    /**
     * 喜欢
     * @param publishId publishId
     * @return          long
     */
    @GetMapping("/{id}/love")
    public ResponseEntity<Long> love(@PathVariable("id") String publishId){
        Long total = commentService.love(publishId);
        return ResponseEntity.ok(total);
    }

    /**
     * 取消喜欢
     * @param publishId publishId
     * @return          long
     */
    @GetMapping("/{id}/unlove")
    public ResponseEntity<Long> unlove(@PathVariable("id") String publishId){
        Long total = commentService.unlove(publishId);
        return ResponseEntity.ok(total);
    }

    /**
     * 动态详情
     * @param publishId publishId
     * @return          vo
     */
    @GetMapping("/{id}")
    public ResponseEntity<MomentVo> queryById(@PathVariable("id") String publishId){
        MomentVo momentVo = movementService.queryById(publishId);
        return ResponseEntity.ok(momentVo);
    }

}
