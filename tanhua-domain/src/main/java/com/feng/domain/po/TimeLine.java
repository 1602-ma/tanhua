package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/4 21:12
 */
@Data
public class TimeLine implements Serializable {

    private ObjectId id;

    /** 好友id */
    private Long userId;

    /** 发布id */
    private ObjectId publishId;

    /** 发布的时间 */
    private Long created;
}
