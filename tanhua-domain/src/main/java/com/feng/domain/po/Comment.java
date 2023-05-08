package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/8 21:35
 */
@Data
@Document(collection = "quanzi_comment")
public class Comment implements Serializable {

    private ObjectId id;

    /** 发布id */
    private ObjectId publishId;

    /** 评论类型，1-点赞，2-评论，3-喜欢 */
    private Integer commentType;

    /** 评论内容类型： 1-对动态操作 2-对视频操作 3-对评论操作 */
    private Integer pubType;

    /** 评论内容 */
    private String content;

    /** 评论人 */
    private Long userId;

    /** 点赞数 */
    private Integer likeCount = 0;

    /** 发表时间 */
    private Long created;

    /** 动态选择更新的字段 */
    public String getCol() {
        return this.commentType == 1 ? "likeCount" : commentType==2? "commentCount"
                : "loveCount";
    }
}
