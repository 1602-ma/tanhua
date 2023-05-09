package com.feng.dubbo.api;

import com.feng.domain.po.FollowUser;
import com.feng.domain.po.Video;
import com.feng.domain.vo.PageResult;

/**
 * @author f
 * @date 2023/5/9 22:23
 */
public interface VideoApi {

    /**
     * save
     * @param video video
     */
    void save(Video video);

    /**
     * 小视频分页查询
     * @param page
     * @param pagesize
     * @return
     */
    PageResult findPage(int page, int pagesize);

    /**
     * 关注用户
     * @param followUser
     */
    void followUser(FollowUser followUser);

    /**
     * 取消关系
     * @param followUser
     */
    void unfollowUser(FollowUser followUser);
}
