package com.feng.server.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.feng.domain.po.RecommendUser;
import com.feng.domain.po.UserInfo;
import com.feng.domain.ro.RecommendUserQueryParam;
import com.feng.domain.vo.PageResult;
import com.feng.domain.vo.TodayBestVo;
import com.feng.dubbo.api.RecommendUserApi;
import com.feng.dubbo.api.UserInfoApi;
import com.feng.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 推荐列表
     * @param param param
     * @return      page
     */
    public PageResult<TodayBestVo> recommendList(RecommendUserQueryParam param) {
        Long userId = UserHolder.getUserId();

        PageResult result = recommendUserApi.findPage(param.getPage(), param.getPagesize(), userId);
        List<RecommendUser> records = (List<RecommendUser>)result.getItems();

        // 默认
        if (CollectionUtils.isEmpty(records)) {
            result = new PageResult<RecommendUser>(10L, param.getPagesize().longValue(), 1L, 1L, null);
            records = defaultRecommend();
        }

        List<TodayBestVo> todayBestVoList = new ArrayList<>();
        for (RecommendUser record : records) {
            TodayBestVo best = new TodayBestVo();

            //补全用户信息
            UserInfo userInfo = this.userInfoApi.findById(record.getUserId());
            BeanUtils.copyProperties(userInfo, best);
            best.setId(record.getUserId());
            best.setFateValue(record.getScore().longValue());
            best.setTags(StringUtils.split(userInfo.getTags(), ","));
            todayBestVoList.add(best);
        }

        result.setItems(todayBestVoList);
        return result;
    }

    /**
     * 构造默认数据
     * @return list
     */
    private List<RecommendUser> defaultRecommend() {
        String ids = "1,2,3,4,5,6,7,8,9,10";
        List<RecommendUser> list = new ArrayList<>();
        RecommendUser user = new RecommendUser();
        for (String s : ids.split(",")) {
            user = new RecommendUser();
            user.setUserId(Long.valueOf(s));
            user.setScore(RandomUtils.nextDouble(70, 98));
            list.add(user);
        }
        return list;
    }
}
