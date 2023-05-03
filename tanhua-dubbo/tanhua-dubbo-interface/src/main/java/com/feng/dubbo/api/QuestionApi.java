package com.feng.dubbo.api;

import com.feng.domain.po.Question;

/**
 * @author f
 * @date 2023/5/3 10:55
 */
public interface QuestionApi {

    /**
     * 根据用户id查询通知配置
     * @param userId userId
     * @return       question
     */
    Question findByUserId(Long userId);

    /**
     * save
     * @param question question
     */
    void save(Question question);

    /**
     * update
     * @param question question
     */
    void update(Question question);
}
