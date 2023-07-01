package com.tygy.bishe.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.tygy.bishe.dto.PageBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *      @Accessors(chain = true)开启链式编程，此注解来自Lombok 加上会导致easyExcel导入字段数据接收不到
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("roomuse")
@ApiModel(value="Roomuse对象", description="")
@ExcelIgnoreUnannotated
public class Roomuse extends PageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "使用教师")
    @ExcelProperty("使用教师")
    private String teacher;

    @ApiModelProperty(value = "课程名称")
    @ExcelProperty("课程名称")
    private String course;

    @ApiModelProperty(value = "班级")
    @ExcelProperty("使用班级")
    private String classes;

    @ApiModelProperty(value = "教室id")
    private Long roomid;

    @ApiModelProperty(value = "申请人")
    @ExcelProperty("申请人")
    private String apply;

    @ApiModelProperty(value = "申请人学号（工号）")
    @ExcelProperty("申请人学号（工号）")
    private Integer sno;

    @ApiModelProperty(value = "周次")
    @ExcelProperty("使用周次")
    private Integer weeks;

    @ApiModelProperty(value = "星期")
    @ExcelProperty("使用星期")
    private Integer week;

    @ApiModelProperty(value = "节次")
    @ExcelProperty("使用节次")
    private Integer section;

    @ApiModelProperty(value = "使用前图片")
    private String beforeuse;

    @ApiModelProperty(value = "使用后图片")
    private String afteruse;


}
