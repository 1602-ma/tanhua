package com.feng.dubbo.api;

import com.feng.domain.po.FollowUser;
import com.feng.domain.po.Video;
import com.feng.domain.vo.PageResult;
import com.feng.dubbo.util.IdService;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author f
 * @date 2023/5/9 22:23
 */
@Service
public class VideoApiImpl implements VideoApi{

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private IdService idService;

    /**
     * save
     * @param video video
     */
    @Override
    public void save(Video video) {
        video.setId(ObjectId.get());
        video.setCreated(System.currentTimeMillis());
        video.setVid(idService.nextId("video"));
        mongoTemplate.save(video);
    }

    /**
     * 分页查询
     * @param page
     * @param pagesize
     * @return
     */
    @Override
    public PageResult findPage(int page, int pagesize) {
        PageResult pageResult = new PageResult();
        pageResult.setPagesize((long)pagesize);
        pageResult.setPage((long)page);
        Query query = new Query();
        long count = mongoTemplate.count(query, Video.class);
        pageResult.setCounts(count);

        query.with(PageRequest.of(page-1,pagesize)).with(Sort.by(Sort.Order.desc("created")));
        List<Video> videos = mongoTemplate.find(query, Video.class);

        pageResult.setItems(videos);

        return pageResult;
    }

    /**
     * 关注用户
     * @param followUser
     */
    @Override
    public void followUser(FollowUser followUser) {
        followUser.setId(ObjectId.get());
        followUser.setCreated(System.currentTimeMillis());
        mongoTemplate.save(followUser);
    }

    /**
     * 取消关注
     * @param followUser
     */
    @Override
    public void unfollowUser(FollowUser followUser) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(followUser.getUserId())
                .and("followUserId").is(followUser.getFollowUserId()));
        mongoTemplate.remove(query,FollowUser.class);
    }
}
