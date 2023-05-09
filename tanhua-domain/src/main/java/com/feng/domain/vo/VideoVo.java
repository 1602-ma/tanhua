package com.feng.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/9 22:38
 */
@Data
public class VideoVo implements Serializable {

    private String id;
    private Long userId;
    private String avatar;
    private String nickname;
    private String cover;
    private String videoUrl;
    private String signature;
    private Integer likeCount;
    private Integer hasLiked;
    private Integer hasFocus;
    private Integer commentCount;
}
