package com.feng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author f
 * @date 2023/5/2 22:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    /** 总记录数 */
    private Long counts;

    /** 每页大小 */
    private Long pagesize;

    /** 总页数 */
    private Long pages;

    /** 页码 */
    private Long page;

    private List<T> items = Collections.emptyList();
}
