package com.feng.server.controller;

import com.feng.domain.ro.RecommendUserQueryParam;
import com.feng.domain.vo.PageResult;
import com.feng.domain.vo.TodayBestVo;
import com.feng.server.service.TodayBestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 推荐
     * @param param param
     * @return      res
     */
    @GetMapping("/recommendation")
    public ResponseEntity recommendList(RecommendUserQueryParam param) {
        PageResult<TodayBestVo> pageResult = todayBestService.recommendList(param);
        return ResponseEntity.ok(pageResult);
    }
}
