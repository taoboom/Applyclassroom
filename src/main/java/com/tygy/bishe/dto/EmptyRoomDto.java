/*
 *太原工业学院 计算机系 版权所有
 *
 */
package com.tygy.bishe.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.tygy.bishe.entity.Roomuse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qinyuxiang
 * @version 1.0
 * @date 2023-04-17
 */
@Data
public class EmptyRoomDto extends RoomuseDto implements Serializable {

    //教室地址
    @ApiModelProperty(value = "教室地址")
    private String address;

    @ApiModelProperty(value = "教室类型id")
    private Integer typeid;

    @ApiModelProperty(value = "周次")
    private Integer[] weeksList;

    @ApiModelProperty(value = "星期")
    private Integer[] weekList;

    @ApiModelProperty(value = "节次")
    private Integer[] sectionList;

}
