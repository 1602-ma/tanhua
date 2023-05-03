package com.feng.domain.po;

import lombok.Data;

/**
 * @author f
 * @date 2023/5/2 22:04
 */
@Data
public class BlackList extends BasePojo{

    private Long id;
    private Long userId;
    private Long blackUserId;
}
