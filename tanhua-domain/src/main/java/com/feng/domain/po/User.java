package com.feng.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author f
 * @date 2023/4/30 11:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tb_user")
public class User extends BasePojo implements Serializable{

    private Long id;

    /** 手机号 */
    private String mobile;

    /** 密码，json序列化时忽略 */
    private String password;
}
