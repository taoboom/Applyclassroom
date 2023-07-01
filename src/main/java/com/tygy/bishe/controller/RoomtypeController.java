package com.tygy.bishe.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tygy.bishe.dto.Result;
import com.tygy.bishe.entity.Role;
import com.tygy.bishe.entity.Roomtype;
import com.tygy.bishe.service.IRoleService;
import com.tygy.bishe.service.IRoomtypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-03-27
 */
@RestController
@RequestMapping("/roomtype")
public class RoomtypeController {

    @Resource
    private IRoomtypeService roomtypeService;

    /**
     * 根据条件查询教室类型
     * @param info
     * @return
     */
    @GetMapping("/getRoomTypeData")
    public Result getRoomTypeData(Roomtype info) {

        QueryWrapper<Roomtype> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(info.getRoomtype() != null, "roomtype", info.getRoomtype());
        final List<Roomtype> list = roomtypeService.list(queryWrapper);

        return Result.ok(list);
    }

    /**
     * 更新或者新增教室类型
     * @param info
     * @return
     */
    @PutMapping("/addOrUpdateRoomType")
    public Result addOrUpdateRoomType(@RequestBody Roomtype info) {
        final boolean flag = roomtypeService.saveOrUpdate(info);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }

    /**
     * 删除教室类型
     * @param info
     * @return
     */
    @DeleteMapping("/delRoomType")
    public Result delRoomType(@RequestBody Roomtype info) {
        final boolean flag = roomtypeService.removeById(info);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }


}
