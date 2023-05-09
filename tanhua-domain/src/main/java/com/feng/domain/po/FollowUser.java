package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/9 22:50
 */
@Data
@Document(collection = "follow_user")
public class FollowUser implements Serializable {

    private ObjectId id;
    private Long userId;
    private Long followUserId;
    private Long created;
}
