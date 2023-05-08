package com.feng.domain.vo;

import lombok.Data;

/**
 * @author f
 * @date 2023/5/8 21:37
 */
@Data
public class CommentVo {

    /** 评论id */
    private String id;

    /** 头像 */
    private String avatar;

    /** 昵称 */
    private String nickname;

    /** 评论 */
    private String content;

    /** 评论时间: 08:27 */
    private String createDate;

    /** 点赞数 */
    private Integer likeCount;

    /** 是否点赞（1是，0否） */
    private Integer hasLiked;
}
