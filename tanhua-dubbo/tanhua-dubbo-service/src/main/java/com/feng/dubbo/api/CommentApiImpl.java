package com.feng.dubbo.api;

import com.feng.domain.po.Comment;
import com.feng.domain.po.Publish;
import com.feng.domain.vo.PageResult;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * 评论处理
 * @author f
 * @date 2023/5/8 21:40
 */
@Service
public class CommentApiImpl implements CommentApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public long save(Comment comment) {
        comment.setId(new ObjectId());
        comment.setCreated(System.currentTimeMillis());
        mongoTemplate.save(comment);
        updateCount(comment, 1);
        long count = getCount(comment);
        return count;
    }

    @Override
    public long remove(Comment comment) {
        Query removeQuery = new Query();
        removeQuery.addCriteria(Criteria.where("publishId").is(comment.getPublishId())
                .and("commentType").is(comment.getCommentType())
                .and("userId").is(comment.getUserId()));
        mongoTemplate.remove(removeQuery, Comment.class);
        updateCount(comment, -1);
        return getCount(comment);
    }

    /**
     * 更新动态表中对应列的计数值
     * @param comment comment
     * @param value   value
     */
    private void updateCount(Comment comment, int value) {
        Query updateQuery = new Query();
        updateQuery.addCriteria(Criteria.where("id").is(comment.getPublishId()));
        Update update = new Update();
        update.inc(comment.getCol(), value);

        Class<?> cls = Publish.class;
        if (comment.getPubType() == 3) {
            cls = Comment.class;
        }
        mongoTemplate.updateFirst(updateQuery, update, cls);
    }

    /**
     * 根据类型统计评论数量
     * @param comment comment
     * @return        long
     */
    private long getCount(Comment comment) {
        Query query = new Query(Criteria.where("id").is(comment.getPublishId()));
        if (comment.getPubType() == 1) {
            Publish publish = mongoTemplate.findOne(query, Publish.class);
            if (comment.getCommentType() == 1) {
                return (long)publish.getLikeCount();
            }
            if (comment.getCommentType() == 2) {
                return (long)publish.getCommentCount();
            }
            if (comment.getCommentType() == 3) {
                return (long)publish.getLoveCount();
            }
        }
        if (comment.getPubType() == 3) {
            Comment cm = mongoTemplate.findOne(query, Comment.class);
            return (long)cm.getLikeCount();
        }
        return 99L;
    }

    /**
     * 分页查询
     *
     * @param page          page
     * @param pageSize      pageSize
     * @param publishId     publishId
     * @return              res
     */
    @Override
    public PageResult findPage(Integer page, Integer pageSize, String publishId) {
        //构建分页结果
        PageResult pageResult = new PageResult();
        pageResult.setPage((long)page);
        pageResult.setPagesize((long)pageSize);

        Query query = new Query();
        // 查询条件，属于某个动态，且评论类型为2 即评论信息
        query.addCriteria(Criteria.where("publishId").is(new ObjectId(publishId))
                .and("commentType").is(2));
        // 统计总数
        long count = mongoTemplate.count(query, Comment.class);
        pageResult.setCounts(count);
        // 查询分页结果集，按创建时间降序
        query.with(PageRequest.of(page-1,pageSize)).with(Sort.by(Sort.Order.desc("created")));
        List<Comment> comments = mongoTemplate.find(query, Comment.class);
        pageResult.setItems(comments);
        return pageResult;
    }
}
