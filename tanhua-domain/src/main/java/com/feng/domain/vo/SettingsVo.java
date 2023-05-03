package com.feng.domain.vo;

import lombok.Data;

/**
 * @author f
 * @date 2023/5/2 22:06
 */
@Data
public class SettingsVo {

    private Long id;
    private String strangerQuestion;
    private String phone;
    private Boolean likeNotification;
    private Boolean pinglunNotification;
    private Boolean gonggaoNotification;
}
