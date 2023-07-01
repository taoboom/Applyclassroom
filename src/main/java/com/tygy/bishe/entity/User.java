package com.tygy.bishe.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.tygy.bishe.dto.PageBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
@ExcelIgnoreUnannotated
@ApiModel(value="User对象", description="")
public class User extends PageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "学号（工号）")
    @ExcelProperty("学号(工号)")
    private Integer sno;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    @ApiModelProperty(value = "密码")
    private String password;

    @ExcelProperty("姓名")
    private String name;

    @ApiModelProperty(value = "角色id")
    private Integer roleid;

    @ApiModelProperty(value = "备注")
    @ExcelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "状态（启用，禁用）")
    private Integer status;

    @ApiModelProperty(value = "电话")
    @ExcelProperty("电话")
    private String phone;

}
