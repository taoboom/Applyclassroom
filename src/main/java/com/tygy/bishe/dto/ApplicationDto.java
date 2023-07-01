/*
 *太原工业学院 计算机系 版权所有
 *
 */
package com.tygy.bishe.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.tygy.bishe.entity.Application;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qinyuxiang
 * @version 1.0
 * @date 2023-04-18
 */
@Data
public class ApplicationDto extends Application implements Serializable {

    @ApiModelProperty(value = "节次")
    private Integer[] sectionList;

    @ApiModelProperty(value = "教室地址")
    private String address;

    @ApiModelProperty(value = "教室类型id")
    private Integer typeid;

    //教室名称
    @ApiModelProperty(value = "教室名称")
    private String roomname;


}
