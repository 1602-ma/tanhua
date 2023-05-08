package com.feng.dubbo.api;

import com.feng.domain.po.Comment;
import com.feng.domain.vo.PageResult;

/**
 * @author f
 * @date 2023/5/8 21:39
 */
public interface CommentApi {

    /**
     * save
     * @param comment comment
     * @return        long
     */
    long save(Comment comment);

    /**
     * delete
     * @param comment comment
     * @return        long
     */
    long remove(Comment comment);

    /**
     * 分页查询
     * @param page      page
     * @param pageSize  pageSize
     * @param publishId publishId
     * @return          res
     */
    PageResult findPage(Integer page, Integer pageSize, String publishId);
}
