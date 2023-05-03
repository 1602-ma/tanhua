package com.feng.domain.po;

import lombok.Data;

/**
 * @author f
 * @date 2023/5/2 22:03
 */
@Data
public class Question extends BasePojo{

    private Long id;
    private Long userId;

    /** 问题内容 */
    private String txt;
}
