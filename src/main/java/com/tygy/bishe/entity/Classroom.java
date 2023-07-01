package com.tygy.bishe.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("classroom")
@ApiModel(value="Classroom对象", description="")
@ExcelIgnoreUnannotated
public class Classroom extends PageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id")
    private Long id;

    @ExcelProperty("教室地址")
    private String address;

    @ApiModelProperty(value = "教室类型id")
    private Integer typeid;

    @ExcelProperty("教室名称")
    private String roomname;

    @ApiModelProperty(value = "容纳人数")
    @ExcelProperty("容纳人数")
    private Integer contain;

    @ExcelProperty("备注")
    private String remark;

}
