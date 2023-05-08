package com.feng.server.service;

import com.feng.domain.po.Comment;
import com.feng.domain.po.Publish;
import com.feng.domain.po.UserInfo;
import com.feng.domain.vo.CommentVo;
import com.feng.domain.vo.MomentVo;
import com.feng.domain.vo.PageResult;
import com.feng.dubbo.api.CommentApi;
import com.feng.dubbo.api.PublishApi;
import com.feng.dubbo.api.UserInfoApi;
import com.feng.server.interceptor.UserHolder;
import com.feng.server.utils.RelativeDateFormat;
import org.apache.dubbo.config.annotation.Reference;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author f
 * @date 2023/5/8 21:33
 */
@Service
public class CommentService {

    @Reference
    private CommentApi commentApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Resource
    private RedisTemplate redisTemplate;

    @Reference
    private PublishApi publishApi;

    /**
     * 点赞
     * @param publishId publishId
     * @return          long
     */
    public Long like(String publishId) {
        Long userId = UserHolder.getUserId();
        Comment comment = new Comment();

        comment.setUserId(userId);
        comment.setCommentType(1);
        comment.setPubType(1);
        comment.setPublishId(new ObjectId(publishId));

        long total = commentApi.save(comment);
        String key = "publish_like_" + userId + "_" + publishId;

        redisTemplate.opsForValue().set(key, "1");
        return total;
    }

    /**
     * 取消点赞
     * @param publishId publishId
     * @return          long
     */
    public Long unlike(String publishId) {
        Long userId = UserHolder.getUserId();

        Comment comment = new Comment();
        comment.setPublishId(new ObjectId(publishId));
        comment.setUserId(UserHolder.getUserId());
        comment.setCommentType(1);
        comment.setPubType(1);

        long total = commentApi.remove(comment);
        String key = "publish_like_" + userId + "_" + publishId;
        redisTemplate.delete(key);
        return total;
    }

    /**
     * 喜欢
     * @param publishId publishId
     * @return          long
     */
    public Long love(String publishId) {
        Long userId = UserHolder.getUserId();
        // 创建Comment
        Comment comment = new Comment();
        // 设置点赞属性
        comment.setUserId(userId);
        comment.setCommentType(3);
        comment.setPubType(1);
        comment.setPublishId(new ObjectId(publishId));
        // 调用api添加评论信息，且获得最新点赞数
        Long total = commentApi.save(comment);
        String key = "publish_love_" + userId+"_" + publishId;
        // 记录下点了赞了
        redisTemplate.opsForValue().set(key,"1");
        return total;
    }

    /**
     * 取消喜欢
     * @param publishId publishId
     * @return          long
     */
    public Long unlove(String publishId) {
        Long userId = UserHolder.getUserId();
        // 创建Comment对象
        Comment comment = new Comment();
        // 设置删除条件
        comment.setPublishId(new ObjectId(publishId));
        comment.setUserId(UserHolder.getUserId());
        comment.setCommentType(3);
        comment.setPubType(1);
        // 调用api删除
        long total = commentApi.remove(comment);
        String key = "publish_love_" + userId+"_" + publishId;
        // 移除redis中记录
        redisTemplate.delete(key);
        return total;
    }

    /**
     * 动态评论列表分页查询
     * @param movementId movementId
     * @param page       page
     * @param pagesize   pageSize
     * @return           res
     */
    public PageResult<CommentVo> findPage(String movementId, int page, int pagesize) {
        //1、调用API查询： PageResult中封装的都是Comment对象
        PageResult<Comment> pageResult = commentApi.findPage(page,pagesize,movementId);
        //2、获取page对象中的数据列表  list<Comment>
        List<Comment> commentList = pageResult.getItems();
        //3、一个Comment对象转化为一个VO对象
        List<CommentVo> commentListVo = new ArrayList<>();
        if(commentList != null &&commentList.size()>0){
            for (Comment comment : commentList) {
                CommentVo commentVo = new CommentVo();
                BeanUtils.copyProperties(comment,commentVo);
                //4、查询发送评论用户详情
                UserInfo userInfo = userInfoApi.findById(comment.getUserId());
                BeanUtils.copyProperties(userInfo,commentVo);
                //设置评论id
                commentVo.setId(comment.getId().toHexString());
                commentVo.setCreateDate(new DateTime(comment.getCreated()).toString("yyyy年MM月dd日 HH:mm"));
                commentVo.setHasLiked(0);
                commentListVo.add(commentVo);
            }
        }
        //5、构造返回值
        PageResult<CommentVo> voPageResult = new PageResult<>();
        BeanUtils.copyProperties(pageResult,voPageResult);
        voPageResult.setItems(commentListVo);
        return voPageResult;
    }

    /**
     * 发表评论
     * @param paramMap
     *  movementId: 动态编号
     *  comment: 评论内容
     */
    public void add(Map<String, String> paramMap) {
        String content = paramMap.get("comment"); // 评论内容
        String publishId = paramMap.get("movementId");
        Comment comment = new Comment();
        comment.setPublishId(new ObjectId(publishId));
        comment.setCommentType(2);
        comment.setPubType(1);
        comment.setContent(content);
        comment.setUserId(UserHolder.getUserId());
        commentApi.save(comment);
    }

    /**
     * 评论点赞
     * @param commentId
     * @return
     */
    public Long likeComment(String commentId) {
        Long userId = UserHolder.getUserId();
        // 创建Comment
        Comment comment = new Comment();
        // 设置点赞属性
        comment.setUserId(userId);
        comment.setCommentType(1); // 点赞
        comment.setPubType(3); // 对评论操作
        comment.setPublishId(new ObjectId(commentId));
        // 调用api添加评论信息，且获得最新点赞数
        Long total = commentApi.save(comment);
        String key = "comment_like_" + userId+"_" + commentId;
        // 记录下点了赞了
        redisTemplate.opsForValue().set(key,"1");
        return total;
    }

    /**
     * 取消对评论的点赞
     * @param commentId
     * @return
     */
    public Long unlikeComment(String commentId) {
        Long userId = UserHolder.getUserId();
        // 创建Comment对象
        Comment comment = new Comment();
        // 设置删除条件
        comment.setPublishId(new ObjectId(commentId));
        comment.setUserId(UserHolder.getUserId());
        comment.setCommentType(1);
        comment.setPubType(3);
        // 调用api删除
        long total = commentApi.remove(comment);
        String key = "comment_like_" + userId+"_" + commentId;
        // 移除redis中记录
        redisTemplate.delete(key);
        return total;
    }
}
