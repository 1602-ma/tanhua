package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * @author f
 * @date 2023/5/4 21:05
 */
@Data
@Document(collection = "quanzi_publish")
public class Publish implements Serializable {

    /** 主键id */
    private ObjectId id;

    /** Long类型，用于推荐系统的模型 */
    private Long pid;

    private Long userId;

    /** 文字 */
    private String textContent;

    /** 媒体数据，图片或小视频 url */
    private List<String> medias;

    /** 谁可以看，1-公开，2-私密，3-部分可见，4-不给谁看 */
    private Integer seeType;

    /** 经度 */
    private String longitude;

    /** 纬度 */
    private String latitude;

    /** 位置名称 */
    private String locationName;

    /** 发布时间 */
    private Long created;

    /** 点赞数 */
    private Integer likeCount=0;

    /** 评论数 */
    private Integer commentCount=0;

    /** 喜欢数 */
    private Integer loveCount=0;
}
