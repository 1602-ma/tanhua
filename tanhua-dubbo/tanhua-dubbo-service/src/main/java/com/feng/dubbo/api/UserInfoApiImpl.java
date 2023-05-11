package com.feng.dubbo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.domain.po.UserInfo;
import com.feng.domain.vo.PageResult;
import com.feng.dubbo.mapper.UserInfoMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.Map;

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

    @Override
    public PageResult<UserInfo> findPage(Map<String, Object> map) {
        Long page = (long)map.get("page");
        Long pageSize = (long)map.get("pagesize");
        IPage<UserInfo> userInfoIPage = userInfoMapper.selectPage(new Page<>(page, pageSize), null);
        PageResult<UserInfo> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setPagesize(pageSize);
        pageResult.setCounts(userInfoIPage.getTotal());
        pageResult.setItems(userInfoIPage.getRecords());
        return pageResult;
    }
}
