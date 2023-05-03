package com.feng.dubbo.api;

import com.feng.domain.po.RecommendUser;
import com.google.errorprone.annotations.Var;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/3 16:02
 */
@Service
public class RecommendUserApiImpl implements RecommendUserApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RecommendUser queryWithMaxScore(Long userId) {
        Criteria criteria = Criteria.where("toUserId").is(userId);

        Query query = new Query(criteria)
                .with(Sort.by(Sort.Order.desc("score")))
                .limit(1);

        return mongoTemplate.findOne(query, RecommendUser.class);
    }
}
