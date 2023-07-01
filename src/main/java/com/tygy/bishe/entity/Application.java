package com.tygy.bishe.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
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
 * @since 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("application")
@ApiModel(value="Application对象", description="")
public class Application extends PageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "申请人学号（工号）")
    private Integer sno;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "教室id")
    private Long roomid;

    @ApiModelProperty(value = "使用班级")
    private String classes;

    @ApiModelProperty(value = "周次")
    private Integer weeks;

    @ApiModelProperty(value = "星期")
    private Integer week;

    @ApiModelProperty(value = "节次")
    private Integer section;

    @ApiModelProperty(value = "申请原因")
    private String reason;

    @ApiModelProperty(value = "审核人")
    private String checker;

    @ApiModelProperty(value = "一审状态(0:未审核，1：通过，2：不通过)")
    private Integer state;

    @ApiModelProperty(value = "二审状态(0:未审核，1：通过，2：不通过)")
    private Integer state2;

    @ApiModelProperty(value = "未通过原因")
    private String failreason;

    @ApiModelProperty(value = "审核时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime audittime;

    @ApiModelProperty(value = "申请时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime applytime;

}
