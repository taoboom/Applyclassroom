package com.tygy.bishe.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
 * @since 2023-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("roomtype")
@ApiModel(value="Roomtype对象", description="")
public class Roomtype implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教室类型id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "教室类型名称")
    private String roomtype;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态（1：启用0：禁用）")
    private Integer status;


}
