package com.feng.mannage.service;

import cn.hutool.core.date.DateTime;
import com.feng.domain.po.Comment;
import com.feng.domain.po.Publish;
import com.feng.domain.po.UserInfo;
import com.feng.domain.po.Video;
import com.feng.domain.vo.CommentVo;
import com.feng.domain.vo.MomentVo;
import com.feng.domain.vo.PageResult;
import com.feng.domain.vo.VideoVo;
import com.feng.dubbo.api.CommentApi;
import com.feng.dubbo.api.PublishApi;
import com.feng.dubbo.api.UserInfoApi;
import com.feng.dubbo.api.VideoApi;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author f
 * @date 2023/5/11 21:55
 */
@Service
public class UserService {

    @Reference
    private UserInfoApi userInfoApi;

    @Reference
    private VideoApi videoApi;

    @Reference
    private PublishApi publishApi;

    @Reference
    private CommentApi commentApi;

    @Resource
    private RedisTemplate redisTemplate;

    public PageResult<UserInfo> findPage(Map<String, Object> paramMap) {
        PageResult<UserInfo> pageResult = userInfoApi.findPage(paramMap);
        return pageResult;
    }

    public UserInfo findById(long userId) {
        return userInfoApi.findById(userId);
    }

    //获取当前用户的所有视频分页列表
    public ResponseEntity findAllVideos(int page, int pagesize, Long uid) {
        //1、根据用户id查询所有的视频分页列表  PageResult
        PageResult result = videoApi.findAll(page,pagesize,uid);
        //2、获取分页数据中数据列表   List<Video>
        List<Video> items = (List<Video>) result.getItems();
        //3、循环遍历数据集合，一个video构造一个vo
        List<VideoVo> list = new ArrayList<>();
        if(items != null) {
            for (Video item : items) {
                UserInfo info = userInfoApi.findById(item.getUserId());
                VideoVo vo = new VideoVo();
                BeanUtils.copyProperties(info,vo);
                BeanUtils.copyProperties(item,vo);
                vo.setCover(item.getPicUrl());
                vo.setId(item.getId().toHexString());
                vo.setSignature(item.getText());//签名
                list.add(vo);
            }
        }
        //4、存入pageResult中
        result.setItems(list);
        //5、构造返回值
        return ResponseEntity.ok(result);
    }

    //获取当前用户的所有动态分页列表
    public ResponseEntity findAllMovements(int page, int pagesize, Long uid, Integer publishState) {
        //1、调用API查询publish
        PageResult result = publishApi.findAll(page,pagesize,uid,publishState);
        //3、获取publish列表
        List<Publish> items = (List<Publish>) result.getItems();
        //4、一个publish构造成一个Movements
        List<MomentVo> list = new ArrayList<>();
        if(items != null) {
            for (Publish item : items) {
                MomentVo vo = new MomentVo();
                UserInfo userInfo = userInfoApi.findById(item.getUserId());
                if(userInfo != null) {
                    BeanUtils.copyProperties(userInfo,vo);
                    if(userInfo.getTags() != null) {
                        vo.setTags(userInfo.getTags().split(","));
                    }
                }
                BeanUtils.copyProperties(item, vo);
                vo.setId(item.getId().toHexString());
                vo.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(item.getCreated())));
                vo.setImageContent(item.getMedias().toArray(new String[]{}));
                vo.setDistance("50米");
                list.add(vo);
            }
        }
        //5、构造返回值
        result.setItems(list);
        return ResponseEntity.ok(result);
    }

    //根据id，查询动态详情
    public ResponseEntity findMovementById(String publishId) {
        //1、根据api查询  publish
        Publish publish = publishApi.findById(publishId);
        //2、转化Movements
        MomentVo vo = new MomentVo();
        //3、构造返回值
        UserInfo userInfo = userInfoApi.findById(publish.getUserId());
        if(userInfo != null) {
            BeanUtils.copyProperties(userInfo,vo);
            if(userInfo.getTags() != null) {
                vo.setTags(userInfo.getTags().split(","));
            }
        }
        BeanUtils.copyProperties(publish, vo);
        vo.setId(publish.getId().toHexString());
        vo.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(publish.getCreated())));
        vo.setImageContent(publish.getMedias().toArray(new String[]{}));
        vo.setDistance("50米");
        return ResponseEntity.ok(vo);
    }

    //查询动态的评论列表
    public ResponseEntity findAllComments(int page, int pagesize, String messageID) {
        //1、调用API查询： PageResult中封装的都是Comment对象
        PageResult result = commentApi.findPage(page,pagesize, messageID);
        //2、获取page对象中的数据列表  list<Comment>
        List<Comment> items = (List<Comment>) result.getItems();
        //3、一个Comment对象转化为一个VO对象
        List<CommentVo> list = new ArrayList<>();
        for (Comment comment : items) {
            CommentVo vo = new CommentVo();
            //4、查询发送评论用户详情
            BeanUtils.copyProperties(comment, vo);
            vo.setId(comment.getId().toString());
            vo.setCreateDate(new DateTime(comment.getCreated()).toString("yyyy年MM月dd日 HH:mm"));
            UserInfo info = userInfoApi.findById(comment.getUserId());
            BeanUtils.copyProperties(info, vo);
            vo.setHasLiked(0); //是否点赞
            list.add(vo);
        }
        //5、构造返回值
        result.setItems(list);
        return ResponseEntity.ok(result);
    }
}
