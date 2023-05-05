package com.feng.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.domain.po.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author f
 * @date 2023/5/3 21:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tb_announcement")
public class Announcement extends BasePojo {
    private String id;
    private String title;
    private String description;
}
