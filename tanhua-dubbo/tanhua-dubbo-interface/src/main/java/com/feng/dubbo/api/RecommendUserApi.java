package com.feng.dubbo.api;

import com.feng.domain.po.RecommendUser;

/**
 * @author f
 * @date 2023/5/3 16:01
 */
public interface RecommendUserApi {

    /**
     * 有缘人
     * @param userId userId
     * @return       recommendUser
     */
    RecommendUser queryWithMaxScore(Long userId);
}
