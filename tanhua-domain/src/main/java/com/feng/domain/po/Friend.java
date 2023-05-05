package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/4 21:13
 */
@Data
@Document(collection = "tanhua_users")
public class Friend implements Serializable {

    private ObjectId id;

    /** 用户id */
    private Long userId;

    /** 好友id */
    private Long friendId;

    /** 时间 */
    private Long created;
}
