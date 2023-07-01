/*
 *太原工业学院 计算机系 版权所有
 *
 */
package com.tygy.bishe.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.tygy.bishe.entity.User;
import lombok.Data;

/**
 * @author qinyuxiang
 * @version 1.0
 * @date 2023-03-26
 */
@Data
public class UserDto extends User {

    //角色名称
    @ExcelProperty("角色")
    private String role;

    private String newPassword;
}
