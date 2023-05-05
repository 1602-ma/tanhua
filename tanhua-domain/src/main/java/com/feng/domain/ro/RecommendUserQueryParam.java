package com.feng.domain.ro;

import lombok.Data;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/3 21:59
 */
@Data
public class RecommendUserQueryParam implements Serializable {

    private Integer page;
    private Integer pagesize;
    private String gender;
    private String lastLogin;
    private Integer age;
    private String city;
    private String education;
}
