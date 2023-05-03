package com.feng.server.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.feng.domain.po.RecommendUser;
import com.feng.domain.po.UserInfo;
import com.feng.domain.vo.TodayBestVo;
import com.feng.dubbo.api.RecommendUserApi;
import com.feng.dubbo.api.UserInfoApi;
import com.feng.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author f
 * @date 2023/5/3 15:56
 */
@Service
@Slf4j
public class TodayBestService {

    @Reference
    private RecommendUserApi recommendUserApi;

    @Reference
    private UserInfoApi userInfoApi;

    /**
     * 今日佳人
     * @return res
     */
    public ResponseEntity queryTodayBest() {
        Long userId = UserHolder.getUserId();
        RecommendUser user = recommendUserApi.queryWithMaxScore(userId);
        if (null == user) {
            user = new RecommendUser();
            user.setUserId(2L);
            user.setScore(90D);
        }

        UserInfo userInfo = userInfoApi.findById(user.getUserId());
        TodayBestVo vo = new TodayBestVo();
        BeanUtils.copyProperties(userInfo, vo);
        vo.setFateValue(user.getScore().longValue());
        if (StringUtils.isNotEmpty(userInfo.getTags())) {
            vo.setTags(userInfo.getTags().split(","));
        }
        log.info("===============================今日佳人：{}", vo);
        return ResponseEntity.ok(vo);
    }
}
