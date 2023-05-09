package com.feng.commons.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author f
 * @date 2023/5/9 23:05
 */
@Data
@AllArgsConstructor
public class HuanXinUser implements Serializable {

    private String username;
    private String password;
    private String nickname;
}
