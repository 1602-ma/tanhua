package com.feng.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author f
 * @date 2023/5/1 22:52
 */
@Data
public abstract class BasePojo implements Serializable {

    @TableField(fill = FieldFill.INSERT)
    public Date created;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    public Date updated;
}
