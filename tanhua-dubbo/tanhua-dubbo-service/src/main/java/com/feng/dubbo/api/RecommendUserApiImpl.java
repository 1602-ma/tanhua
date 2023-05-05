package com.feng.dubbo.api;

import com.feng.domain.po.RecommendUser;
import com.feng.domain.vo.PageResult;
import com.google.errorprone.annotations.Var;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 推荐列表
     * @param page      page
     * @param pageSize  pageSize
     * @param userId    userId
     * @return          page
     */
    @Override
    public PageResult<RecommendUser> findPage(int page, int pageSize, Long userId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("toUserId").is(userId));
        long total = mongoTemplate.count(query, RecommendUser.class);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("score")));
        query.with(pageRequest);

        List<RecommendUser> list = mongoTemplate.find(query, RecommendUser.class);

        PageResult<RecommendUser> pageResult = new PageResult<>();
        pageResult.setItems(list);
        pageResult.setPage(Long.valueOf(page));
        pageResult.setPagesize((long)pageSize);
        pageResult.setCounts(total);
        long pages = total / pageSize;
        pages += total % pageSize > 0 ? 1 : 0;
        pageResult.setPages(pages);

        return pageResult;
    }
}
