package com.feng.dubbo.api;

import com.feng.domain.po.Settings;

/**
 * @author f
 * @date 2023/5/3 10:51
 */
public interface SettingApi {

    /**
     * 根据用户id查询通知配置
     * @param userId userId
     * @return       setting
     */
    Settings findByUserId(Long userId);

    /**
     * save
     * @param settings settings
     */
    void save(Settings settings);

    /**
     * update
     * @param settings settings
     */
    void update(Settings settings);
}
