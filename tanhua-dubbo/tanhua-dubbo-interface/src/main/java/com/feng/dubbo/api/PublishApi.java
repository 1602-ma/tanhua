package com.feng.dubbo.api;

import com.feng.domain.ro.PublishVo;
import com.feng.domain.vo.PageResult;

/**
 * @author f
 * @date 2023/5/4 21:35
 */
public interface PublishApi {

    /**
     * 添加用户动态
     * @param publishVo publishVo
     */
    void add(PublishVo publishVo);

    /**
     * 查询登录用户好友的动态
     * @param page      page
     * @param pageSize  pageSize
     * @param userId    userId
     * @return          list
     */
    PageResult findFriendPublishByTimeLine(int page, int pageSize, Long userId);

    /**
     * 查询推荐动态
     * @param page      page
     * @param size      pageSize
     * @param userId    userId
     * @return          page
     */
    PageResult findRecommendPublish(int page, int size, long userId);
}
