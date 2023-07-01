/*
 *太原工业学院 计算机系 版权所有
 *
 */
package com.tygy.bishe.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.tygy.bishe.entity.Classroom;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qinyuxiang
 * @version 1.0
 * @date 2023-04-01
 */
@Data
public class ClassroomDto extends Classroom implements Serializable {

    //教室类型名称
    @ExcelProperty("教室类型")
    private String roomtype;
}
