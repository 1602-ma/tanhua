package com.feng.dubbo.util;

import com.feng.domain.po.Sequence;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author f
 * @date 2023/5/4 21:31
 */
@Component
public class IdService {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 根据集合名，生成自增id
     * @param collectionName collectionName
     * @return               id
     */
    public Long nextId(String collectionName) {
        Query query = Query.query(Criteria.where("collName").is(collectionName));
        Update update = new Update();
        update.inc("seqId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        Sequence sq = mongoTemplate.findAndModify(query, update, options, Sequence.class);
        return sq.getSeqId();
    }
}
