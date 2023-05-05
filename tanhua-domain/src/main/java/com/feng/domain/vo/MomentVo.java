package com.feng.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/4 21:18
 */
@Data
public class MomentVo implements Serializable {

    /** 动态id */
    private String id;

    /** 用户id */
    private Long userId;

    /** 头像 */
    private String avatar;

    /** 昵称 */
    private String nickname;

    /** 性别 man woman */
    private String gender;

    /** 年龄 */
    private Integer age;

    /** 标签 */
    private String[] tags;

    /** 文字动态 */
    private String textContent;

    /** 图片动态 */
    private String[] imageContent;

    /** 距离 */
    private String distance;

    /** 发布时间 如: 10分钟前 */
    private String createDate;

    /** 点赞数 */
    private int likeCount;

    /** 评论数 */
    private int commentCount;

    /** 喜欢数 */
    private int loveCount;

    /** 是否点赞（1是，0否） */
    private Integer hasLiked;

    /** 是否喜欢（1是，0否） */
    private Integer hasLoved;
}
