package com.feng.dubbo.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.domain.po.UserInfo;
import com.feng.domain.vo.PageResult;

/**
 * @author f
 * @date 2023/5/3 14:41
 */
public interface BlackListApi {

    /**
     * 分页查询黑名单
     * @param page      page
     * @param pageSize  pageSize
     * @param id        id
     * @return          page
     */
    PageResult<UserInfo> findBlackList(int page, int pageSize, Long id);

    /**
     * 根据用户id和黑名单用户id，删除
     * @param userId      userId
     * @param blackUserId blackUserId
     */
    void delete(Long userId, Long blackUserId);
}
