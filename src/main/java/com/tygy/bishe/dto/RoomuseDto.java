/*
 *太原工业学院 计算机系 版权所有
 *
 */
package com.tygy.bishe.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.entity.Roomuse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author qinyuxiang
 * @version 1.0
 * @date 2023-04-04
 */
@Data
public class RoomuseDto  extends Roomuse implements Serializable {

    //教室名称
    @ExcelProperty("教室名称")
    private String roomname;

    private String address;

    private List<String> beforeusePic;

    private List<String> afterusePic;

}
