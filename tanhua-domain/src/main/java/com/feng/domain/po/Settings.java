package com.feng.domain.po;

import lombok.Data;

/**
 * @author f
 * @date 2023/5/2 22:00
 */
@Data
public class Settings extends BasePojo{

    private Long id;
    private Long userId;
    private Boolean likeNotification;
    private Boolean pinglunNotification;
    private Boolean gonggaoNotification;
}
