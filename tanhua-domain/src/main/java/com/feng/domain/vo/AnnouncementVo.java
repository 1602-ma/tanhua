package com.feng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author f
 * @date 2023/5/3 21:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementVo {

    private String id;
    private String title;
    private String description;
    private String createDate;

}
