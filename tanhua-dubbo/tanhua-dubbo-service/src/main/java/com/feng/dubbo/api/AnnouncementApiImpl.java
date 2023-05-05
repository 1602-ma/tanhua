package com.feng.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.feng.domain.po.Announcement;
import com.feng.domain.vo.AnnouncementVo;
import com.feng.domain.vo.PageResult;
import com.feng.dubbo.mapper.AnnouncementMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/3 21:27
 */
@Service
public class AnnouncementApiImpl implements AnnouncementApi{

    @Resource
    private AnnouncementMapper announcementMapper;

    @Override
    public PageResult<Announcement> findAll(int page, int size) {
        Page<Announcement> pages = new Page<>(page, size);
        IPage<Announcement> pageInfo = announcementMapper.selectPage(pages, new QueryWrapper<>());

        PageResult<Announcement> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo.getSize(), pageInfo.getPages(), pageInfo.getCurrent(), pageInfo.getRecords());
        return pageResult;
    }
}
