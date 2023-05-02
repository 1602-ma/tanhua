package com.feng.dubbo.api;

import com.feng.domain.po.UserInfo;
import com.feng.dubbo.mapper.UserInfoMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/2 10:59
 */
@Service
public class UserInfoApiImpl implements UserInfoApi{

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public void save(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public UserInfo findById(Long userId) {
        return userInfoMapper.selectById(userId);
    }
}
