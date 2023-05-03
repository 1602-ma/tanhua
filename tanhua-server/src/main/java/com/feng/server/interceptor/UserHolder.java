package com.feng.server.interceptor;

import com.feng.domain.po.User;

/**
 * 登录用户信息持有者
 * @author f
 * @date 2023/5/2 21:52
 */
public class UserHolder {

    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    /**
     * 向当前线程存入用户数据
     * @param user user
     */
    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    /**
     * 从当前线程中获取用户数据
     * @return user
     */
    public static User getUser() {
        return userThreadLocal.get();
    }

    /**
     * 获取登录用户id
     * @return userId
     */
    public static Long getUserId() {
        return userThreadLocal.get().getId();
    }
}
