/*
 *太原工业学院 计算机系 版权所有
 *
 */
package com.tygy.bishe.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author qinyuxiang
 * @version 1.0
 * @date 2022/9/25
 */
@Data
public class PageBean {
    /**
     * page 页数   pageSize 每页的条数
     */
    @TableField(exist = false)
    private Integer page;

    @TableField(exist = false)
    private Integer pageSize;

}
