package com.feng.dubbo.api;

import com.feng.domain.po.User;

/**
 * @author f
 * @date 2023/4/30 11:29
 */
public interface UserApi {

    /**
     * 添加用户
     * @param user user
     * @return     id
     */
    Long save(User user);

    /**
     * 通过手机号查询用户
     * @param mobile mobile
     * @return       user
     */
    User findByMobile(String mobile);

    /**
     * 修改手机号码
     * @param userId userId
     * @param phone  phone
     */
    void updateMobile(Long userId, String phone);
}
