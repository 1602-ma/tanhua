package com.feng.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.domain.po.BlackList;
import com.feng.domain.po.UserInfo;
import com.feng.domain.vo.PageResult;
import com.feng.dubbo.mapper.BlackListMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/3 14:43
 */
@Service
public class BlackListApiImpl implements BlackListApi{

    @Resource
    private BlackListMapper blackListMapper;

    @Override
    public PageResult<UserInfo> findBlackList(int page, int pageSize, Long id) {
        Page pageRequest = new Page<>(page, pageSize);
        IPage pageInfo = blackListMapper.findBlackList(pageRequest, id);
        PageResult<UserInfo> pageResult = new PageResult<UserInfo>(pageInfo.getTotal(), pageInfo.getSize(), pageInfo.getPages(), pageInfo.getCurrent(), pageInfo.getRecords());
        return pageResult;
    }

    @Override
    public void delete(Long userId, Long blackUserId) {
        LambdaQueryWrapper<BlackList> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BlackList::getUserId, userId)
                .eq(BlackList::getBlackUserId, blackUserId);
        blackListMapper.delete(lambdaQueryWrapper);
    }
}
