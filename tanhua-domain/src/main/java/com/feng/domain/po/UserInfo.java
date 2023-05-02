package com.feng.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author f
 * @date 2023/5/2 10:42
 */
@Data
public class UserInfo extends BasePojo{

    /** 用户id */
    @TableId(type= IdType.INPUT)
    private Long id;

    /** 昵称 */
    private String nickname;

    /** 用户头像 */
    private String avatar;

    /** 生日 */
    private String birthday;

    /** 性别 */
    private String gender;

    /** 年龄 */
    private Integer age;

    /** 城市 */
    private String city;

    /** 收入 */
    private String income;

    /** 学历 */
    private String education;

    /** 行业 */
    private String profession;

    /** 婚姻状态 */
    private Integer marriage;

    /** 用户标签：多个用逗号分隔 */
    private String tags;

    /** 封面图片 */
    private String coverPic;
}
