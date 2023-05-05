package com.feng.dubbo.api;

import com.feng.domain.po.Announcement;
import com.feng.domain.vo.AnnouncementVo;
import com.feng.domain.vo.PageResult;

/**
 * @author f
 * @date 2023/5/3 21:25
 */
public interface AnnouncementApi {

    /**
     * 查询公告
     * @param page page
     * @param size size
     * @return     page
     */
    PageResult<Announcement> findAll(int page, int size);
}
