package com.feng.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/3 15:54
 */
@Data
public class TodayBestVo implements Serializable {

    private Long id;
    private String avatar;
    private String nickname;

    /** 性别 man woman */
    private String gender;
    private Integer age;
    private String[] tags;

    /** 缘分值 */
    private Long fateValue;
}
