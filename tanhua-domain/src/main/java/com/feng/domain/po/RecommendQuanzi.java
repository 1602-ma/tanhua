package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/4 21:14
 */
@Data
@Document(collection = "recommend_quanzi")
public class RecommendQuanzi implements Serializable {

    /** 主键 */
    @Id
    private ObjectId id;

    /** 推荐的用户id */
    @Indexed
    private Long userId;

    private Long pid;

    /** 发布的动态的id */
    private ObjectId publishId;

    /** 推荐分数 */
    @Indexed
    private Double score = 0d;

    /** 日期 */
    private Long created;
}
