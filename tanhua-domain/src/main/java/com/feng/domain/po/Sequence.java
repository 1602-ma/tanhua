package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author f
 * @date 2023/5/4 21:29
 */
@Data
@Document(collection = "sequence")
public class Sequence {

    private ObjectId id;

    /** 自增序列 */
    private long seqId;

    /** 集合名称 */
    private String collName;
}
