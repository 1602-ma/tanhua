package com.feng.server.controller;

import com.feng.server.service.TodayBestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/3 15:55
 */
@RestController
@RequestMapping("/tanhua")
public class TodayBestController {

    @Resource
    private TodayBestService todayBestService;

    /**
     * 今日佳人
     * @return res
     */
    @GetMapping("/todayBest")
    public ResponseEntity todayBest() {
        return todayBestService.queryTodayBest();
    }
}
