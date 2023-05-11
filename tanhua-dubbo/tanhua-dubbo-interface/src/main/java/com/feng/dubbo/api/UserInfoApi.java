package com.feng.dubbo.api;

import com.feng.domain.po.UserInfo;
import com.feng.domain.vo.PageResult;

import java.util.Map;

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

    /**
     * 用户管理 page
     * @param map map
     * @return    page
     */
    PageResult<UserInfo> findPage(Map<String, Object> map);
}
