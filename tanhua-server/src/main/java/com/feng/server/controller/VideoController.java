package com.feng.server.controller;

import com.feng.domain.vo.PageResult;
import com.feng.domain.vo.VideoVo;
import com.feng.server.service.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/9 22:18
 */
@RestController
@RequestMapping("smallVideos")
public class VideoController {

    @Resource
    private VideoService videoService;

    /**
     * 发布小视频
     * @param videoThumbnail video
     * @param videoFile      video
     * @return               res
     */
    @PostMapping()
    public ResponseEntity post(MultipartFile videoThumbnail, MultipartFile videoFile) {
        videoService.post(videoThumbnail, videoFile);
        return ResponseEntity.ok(null);
    }

    /**
     * 分页查询
     * @param page
     * @param pagesize
     * @return
     */
    @GetMapping
    public ResponseEntity findPage(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int pagesize){
        page=page<1?1:page;
        PageResult<VideoVo> pageResult = videoService.findPage(page,pagesize);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 关注视频的作者
     * @param userId
     * @return
     */
    @PostMapping("/{id}/userFocus")
    public ResponseEntity followUser(@PathVariable("id") long userId){
        videoService.followUser(userId);
        return ResponseEntity.ok(null);
    }

    /**
     * 取消关注
     * @param userId
     * @return
     */
    @PostMapping("/{id}/userUnFocus")
    public ResponseEntity unfollowUser(@PathVariable("id") long userId){
        videoService.unfollowUser(userId);
        return ResponseEntity.ok(null);
    }
}
