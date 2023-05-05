package com.feng.domain.ro;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author f
 * @date 2023/5/4 21:16
 */
@Data
public class PublishVo implements Serializable {

    /** 用户id */
    private Long userId;

    /** 文本内容 */
    private String textContent;

    /** 地理位置 */
    private String location;

    /** 经度 */
    private String longitude;

    /** 纬度 */
    private String latitude;

    /** 图片url */
    private List<String> medias;
}
