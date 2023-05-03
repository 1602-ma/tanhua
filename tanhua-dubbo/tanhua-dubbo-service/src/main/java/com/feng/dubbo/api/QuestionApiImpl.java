package com.feng.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.feng.domain.po.Question;
import com.feng.dubbo.mapper.QuestionMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/3 10:56
 */
@Service
public class QuestionApiImpl implements QuestionApi{

    @Resource
    private QuestionMapper questionMapper;

    @Override
    public Question findByUserId(Long userId) {
        LambdaQueryWrapper<Question> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Question::getUserId, userId);
        return questionMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public void save(Question question) {
        questionMapper.insert(question);
    }

    @Override
    public void update(Question question) {
        questionMapper.updateById(question);
    }
}
