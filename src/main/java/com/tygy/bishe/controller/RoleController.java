package com.tygy.bishe.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tygy.bishe.dto.Result;
import com.tygy.bishe.entity.Role;
import com.tygy.bishe.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2023-03-20
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * 根据条件查询角色
     * @param info
     * @return
     */
    @GetMapping("/getRoleData")
    public Result getRoleData(Role info) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(info.getRole() != null, "role", info.getRole());
        final List<Role> list = roleService.list(queryWrapper);

        return Result.ok(list);
    }

    /**
     * 新增或更新角色
     * @param info
     * @return
     */
    @PutMapping("/addOrUpdateRole")
    public Result addOrUpdateRole(@RequestBody Role info) {
        final boolean flag = roleService.saveOrUpdate(info);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }

    /**
     * 删除角色
     * @param info
     * @return
     */
    @DeleteMapping("/delRole")
    public Result delRole(@RequestBody Role info) {
        final boolean flag = roleService.removeById(info);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }

}
