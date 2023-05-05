package com.feng.server.controller;

import com.feng.domain.ro.PublishVo;
import com.feng.domain.vo.MomentVo;
import com.feng.domain.vo.PageResult;
import com.feng.server.interceptor.UserHolder;
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
}
