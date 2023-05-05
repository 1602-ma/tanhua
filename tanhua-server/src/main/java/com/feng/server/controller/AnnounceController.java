package com.feng.server.controller;

import com.feng.server.service.AnnounceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 公告
 * @author f
 * @date 2023/5/3 21:19
 */
@RestController
@RequestMapping("/messages")
public class AnnounceController {

    @Resource
    private AnnounceService announceService;

    /**
     * 获取公告
     * @param page      page
     * @param pageSize  pageSize
     * @return          announcement
     */
    @GetMapping("/announcements")
    public ResponseEntity announcements(@RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10") int pageSize) {
        return announceService.announcements(page, pageSize);
    }
}
