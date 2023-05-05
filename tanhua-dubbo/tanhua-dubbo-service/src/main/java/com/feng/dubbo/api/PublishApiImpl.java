package com.feng.dubbo.api;

import com.feng.domain.po.*;
import com.feng.domain.ro.PublishVo;
import com.feng.domain.vo.PageResult;
import com.feng.dubbo.util.IdService;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author f
 * @date 2023/5/4 21:37
 */
@Service
public class PublishApiImpl implements PublishApi{

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private IdService idService;

    /**
     * 添加用户动态
     * @param publishVo publishVo
     */
    @Override
    public void add(PublishVo publishVo) {
        // 1.保存到发布总表
        long currentTimeMillis = System.currentTimeMillis();
        Publish publish = new Publish();
        BeanUtils.copyProperties(publishVo, publish);
        publish.setId(ObjectId.get());
        publish.setPid(idService.nextId("quanzi_publish"));
        publish.setLocationName(publishVo.getLocation());
        publish.setCreated(currentTimeMillis);
        // 公开
        publish.setSeeType(1);
        mongoTemplate.save(publish);

        // 2.保存到相册
        Album album = new Album();
        album.setCreated(currentTimeMillis);
        album.setPublishId(publish.getId());
        album.setId(ObjectId.get());
        mongoTemplate.save(album, "quanzi_album_" + publish.getUserId());

        // 3.查询好友
        Query query = Query.query(Criteria.where("userId").is(publishVo.getUserId()));
        List<Friend> friends = mongoTemplate.find(query, Friend.class);

        // 4.保存到好友表
        friends.forEach(f -> {
            TimeLine timeLine = new TimeLine();
            timeLine.setCreated(currentTimeMillis);
            timeLine.setPublishId(publish.getId());
            timeLine.setUserId(publish.getUserId());
            timeLine.setId(ObjectId.get());
            mongoTemplate.save(timeLine, "quanzi_time_line_" + f.getFriendId());
        });
    }

    /**
     * 查询登录用户好友动态
     * @param page      page
     * @param pageSize  pageSize
     * @param userId    userId
     * @return
     */
    @Override
    public PageResult findFriendPublishByTimeLine(int page, int pageSize, Long userId) {
        // 1.查询时间线表
        Query query = new Query().with(Sort.by(Sort.Order.desc("created"))).limit(pageSize).skip((page - 1) * pageSize);
        List<TimeLine> lines = mongoTemplate.find(query, TimeLine.class, "quanzi_time_line_" + userId);
        long total = mongoTemplate.count(query, TimeLine.class, "quanzi_time_line_" + userId);

        // 2.循环时间线数据，查询publish,获取动态详情
        List<Publish> list = new ArrayList<>();
        for (TimeLine line : lines) {
            Publish publish = mongoTemplate.findById(line.getPublishId(), Publish.class);
            if (null != publish) {
                list.add(publish);
            }
        }
        long pages = total / pageSize;
        pages += total % pageSize > 0 ? 1 : 0;
        return new PageResult(total,  (long)pageSize, pages, (long)page, list);
    }

    /**
     *
     * @param page      page
     * @param size      pageSize
     * @param userId    userId
     * @return          page
     */
    @Override
    public PageResult findRecommendPublish(int page, int size, long userId) {
        Query query = new Query(Criteria.where("userId").is(userId))
                .with(Sort.by(Sort.Order.desc("created")))
                .limit(size).skip((page - 1) * size);
        List<RecommendQuanzi> recommends = mongoTemplate.find(query, RecommendQuanzi.class);
        long total = mongoTemplate.count(query, RecommendQuanzi.class);
        List<Publish> list = new ArrayList<>();
        for (RecommendQuanzi recommend : recommends) {
            if (null != recommend.getPublishId()) {
                Publish publish = mongoTemplate.findById(recommend.getPublishId(), Publish.class);
                if (null != publish) {
                    list.add(publish);
                }
            }
        }
        long pages = total / size;
        pages += total % size > 0 ? 1 : 0;
        return new PageResult((long)total, (long)size, (long)pages, (long) page, list);
    }
}
