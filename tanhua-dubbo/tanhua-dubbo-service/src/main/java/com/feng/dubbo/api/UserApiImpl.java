package com.feng.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.feng.domain.po.User;
import com.feng.dubbo.mapper.UserMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author f
 * @date 2023/4/30 11:31
 */
@Service
public class UserApiImpl implements UserApi{

    @Resource
    private UserMapper userMapper;

    @Override
    public Long save(User user) {
        user.setCreated(new Date());
        user.setUpdated(new Date());
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public User findByMobile(String mobile) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotEmpty(mobile), User::getMobile, mobile);

        return userMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public void updateMobile(Long userId, String phone) {
        User user = new User();
        user.setMobile(phone);
        user.setId(userId);
        userMapper.updateById(user);
    }
}
