package com.feng.server.service;

import com.feng.commons.templates.OssTemplate;
import com.feng.domain.po.Publish;
import com.feng.domain.po.UserInfo;
import com.feng.domain.ro.PublishVo;
import com.feng.domain.vo.MomentVo;
import com.feng.domain.vo.PageResult;
import com.feng.dubbo.api.PublishApi;
import com.feng.dubbo.api.UserInfoApi;
import com.feng.server.interceptor.UserHolder;
import com.feng.server.utils.RelativeDateFormat;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author f
 * @date 2023/5/4 21:23
 */
@Service
public class MovementService {

    @Resource
    private OssTemplate ossTemplate;

    @Reference
    private PublishApi publishApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 发布动态
     * @param publishVo publish
     * @param image     image
     */
    public void save(PublishVo publishVo, MultipartFile[] image) {
        List<String> media = new ArrayList<>();
        for (MultipartFile multipartFile : image) {
            String filename = multipartFile.getOriginalFilename();
//            String path = ossTemplate.upload(filename, multipartFile.getInputStream());
            String path = "1.jpg";
            media.add(path);
        }

        publishVo.setUserId(UserHolder.getUserId());
        publishVo.setMedias(media);
        publishApi.add(publishVo);
    }

    /**
     * 查询好友动态
     * @param page      page
     * @param pageSize  pageSize
     * @return          list
     */
    public PageResult<MomentVo> queryFriendPublishList(int page, int pageSize) {
        Long userId = UserHolder.getUserId();
        PageResult result = publishApi.findFriendPublishByTimeLine(page, pageSize, userId);
        List<Publish> items = (List<Publish>)result.getItems();

        List<MomentVo> list = new ArrayList<>();
        if (null != items) {
            MomentVo vo = new MomentVo();
            for (Publish item : items) {
                vo = new MomentVo();
                UserInfo userInfo = userInfoApi.findById(item.getUserId());
                if (null != userInfo) {
                    BeanUtils.copyProperties(userInfo, vo);
                    if (null != userInfo.getTags()) {
                        vo.setTags(userInfo.getTags().split(","));
                    }
                }
                BeanUtils.copyProperties(item, vo);
                vo.setId(item.getId().toHexString());
                vo.setCreateDate(RelativeDateFormat.format(new Date(item.getCreated())));
                String key1 = "publish_like_" + userId + "_" + item.getId().toHexString();
                if (redisTemplate.hasKey(key1)) {
                    vo.setHasLiked(1);
                }else {
                    vo.setHasLiked(0);
                }
                vo.setHasLoved(1);
                vo.setImageContent(item.getMedias().toArray(new String[]{}));
                vo.setDistance("1m");
                list.add(vo);
            }
        }
        result.setItems(list);
        return result;
    }

    /**
     * 查询推荐动态
     * @param page      page
     * @param pageSize  pageSize
     * @return          result
     */
    public PageResult<MomentVo> queryRecommendPublishList(int page, int pageSize) {
        Long userId = UserHolder.getUserId();
        PageResult result = publishApi.findRecommendPublish(page, pageSize, userId);
        List<Publish> items = (List<Publish>)result.getItems();
        List<MomentVo> list = new ArrayList<>();
        if (null != items) {
            for (Publish item : items) {
                MomentVo vo = new MomentVo();
                UserInfo userInfo = userInfoApi.findById(item.getUserId());
                if (null != userInfo) {
                    BeanUtils.copyProperties(userInfo, vo);
                    if (null != userInfo.getTags()) {
                        vo.setTags(userInfo.getTags().split(","));
                    }
                }
                BeanUtils.copyProperties(item, vo);
                vo.setId(item.getId().toHexString());
                String key2 = "publish_love_" + userId + "_" + item.getId().toHexString();
                if (redisTemplate.hasKey(key2)) {
                    vo.setHasLoved(1);
                }else {
                    vo.setHasLoved(0);
                }
                String key1 = "publish_like_" + userId + "_" + item.getId().toHexString();
                if (redisTemplate.hasKey(key1)) {
                    vo.setHasLiked(1);
                }else {
                    vo.setHasLiked(0);
                }
                vo.setImageContent(item.getMedias().toArray(new String[]{}));
                vo.setDistance("1m");
                list.add(vo);
            }
        }
        result.setItems(list);
        return result;
    }

    /**
     * 我的动态
     * @param page      page
     * @param pageSize  pageSize
     * @param userId    userId
     * @return          list
     */
    public PageResult<MomentVo> queryMyAlbum(int page, int pageSize, Long userId) {
        PageResult result = publishApi.findMyAlbum(page, pageSize, userId);
        List<Publish> items = (List<Publish>)result.getItems();
        List<MomentVo> list = new ArrayList<>();
        for (Publish item : items) {
            MomentVo vo = new MomentVo();
            UserInfo userInfo = userInfoApi.findById(item.getUserId());
            if (null != userInfo) {
                BeanUtils.copyProperties(userInfo, vo);
                if (null != userInfo.getTags()) {
                    vo.setTags(userInfo.getTags().split(","));
                }
            }
            BeanUtils.copyProperties(item, vo);
            vo.setId(item.getId().toHexString());
            vo.setCreateDate(RelativeDateFormat.format(new Date(item.getCreated())));
            String key1 = "publish_like_" + userId + "_" + item.getId().toHexString();
            if (redisTemplate.hasKey(key1)) {
                vo.setHasLiked(1);
            }else {
                vo.setHasLiked(0);
            }
            vo.setHasLoved(1);
            vo.setImageContent(item.getMedias().toArray(new String[]{}));
            vo.setDistance("1m");
            list.add(vo);


        }
        result.setItems(list);
        return result;
    }

    /**
     * 动态详情
     * @param publishId publishId
     * @return          publish
     */
    public MomentVo queryById(String publishId) {
        //1、调用api查询动态详情 ： publish
        Publish publish = publishApi.findById(publishId);
        //2、创建vo对象
        MomentVo vo = new MomentVo();
        //3、查询用户详情（发布人）
        UserInfo userInfo = userInfoApi.findById(publish.getUserId());
        Long userId = publish.getUserId();
        //4、构造数据
        if(userInfo != null) {
            BeanUtils.copyProperties(userInfo,vo);
            if(userInfo.getTags() != null) {
                vo.setTags(userInfo.getTags().split(","));
            }
        }
        BeanUtils.copyProperties(publish, vo);
        vo.setId(publish.getId().toHexString());
        vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
        vo.setHasLiked(0);  //是否点赞  0：未点 1:点赞
        vo.setHasLoved(0);  //是否喜欢  0：未点 1:点赞
        vo.setImageContent(publish.getMedias().toArray(new String[]{}));
        vo.setDistance("50米");
        return vo;
    }
}
