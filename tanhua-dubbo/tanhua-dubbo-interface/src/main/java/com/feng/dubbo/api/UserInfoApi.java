package com.feng.dubbo.api;

import com.feng.domain.po.UserInfo;

/**
 * @author f
 * @date 2023/5/2 10:58
 */
public interface UserInfoApi {

    /**
     * 保存用户基本信息
     * @param userInfo userInfo
     */
    void save(UserInfo userInfo);

    /**
     * 通过id更新用户信息
     * @param userInfo userInfo
     */
    void update(UserInfo userInfo);

    /**
     * 通过id查询用户基本信息
     * @param userId userId
     * @return       userInfo
     */
    UserInfo findById(Long userId);
}
