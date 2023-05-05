package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/4 21:10
 */
@Data
public class Album implements Serializable {

    /** 主键id */
    private ObjectId id;
    
    /** 发布id */
    private ObjectId publishId;

    /** 发布时间 */
    private Long created;
}
