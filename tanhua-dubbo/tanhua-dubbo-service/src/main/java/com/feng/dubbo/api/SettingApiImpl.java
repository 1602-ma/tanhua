package com.feng.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.feng.domain.po.Settings;
import com.feng.dubbo.mapper.SettingMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/3 10:52
 */
@Service
public class SettingApiImpl implements SettingApi{

    @Resource
    private SettingMapper settingMapper;

    @Override
    public Settings findByUserId(Long userId) {
        LambdaQueryWrapper<Settings> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Settings::getUserId, userId);
        return settingMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public void save(Settings settings) {
        settingMapper.insert(settings);
    }

    @Override
    public void update(Settings settings) {
        settingMapper.updateById(settings);
    }
}
