package com.feng.dubbo.api;

import com.feng.domain.po.RecommendUser;
import com.feng.domain.vo.PageResult;

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

    /**
     * 推荐列表
     * @param page      page
     * @param pageSize  pageSize
     * @param userId    userId
     * @return          page
     */
    PageResult<RecommendUser> findPage(int page, int pageSize, Long userId);
}
