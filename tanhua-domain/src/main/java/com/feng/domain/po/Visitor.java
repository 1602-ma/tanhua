package com.feng.domain.po;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/9 23:13
 */
@Data
@Document(collection = "visitors")
public class Visitor implements Serializable {

    private ObjectId id;
    private Long userId;
    private Long visitorUserId;
    private String from;
    private Long date;

    private Double score;
}
