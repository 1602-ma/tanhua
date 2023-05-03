package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/3 15:59
 */
@Data
@Document(collection = "recommend_user")
public class RecommendUser implements Serializable {

    /** 主键id */
    @Id
    private ObjectId id;

    /** 推荐的用户id */
    @Indexed
    private Long userId;

    /** 登录用户id */
    private Long toUserId;

    /** 推荐得分 */
    @Indexed
    private Double score =0d;

    /** 日期 */
    private String date;
}
