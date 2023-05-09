package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/9 22:22
 */
@Data
@Document(collection = "video")
public class Video implements Serializable {

    private ObjectId id;
    private Long userId;
    private Long vid;

    private String text;
    private String picUrl;
    private String videoUrl;
    private Long created;

    private Integer likeCount=0;
    private Integer commentCount=0;
    private Integer loveCount=0;
}
