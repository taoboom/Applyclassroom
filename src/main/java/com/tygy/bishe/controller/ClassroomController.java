package com.tygy.bishe.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tygy.bishe.dto.ClassroomDto;
import com.tygy.bishe.dto.Result;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.entity.Roomtype;
import com.tygy.bishe.listener.ClassroomDtoListener;
import com.tygy.bishe.service.IClassroomService;
import com.tygy.bishe.service.IRoomtypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-04-01
 */
@RestController
@RequestMapping("/classroom")
@Slf4j
public class ClassroomController {

    @Autowired
    private IClassroomService classroomService;

    @Autowired
    private IRoomtypeService roomtypeService;

    /**
     * 根据条件查询教室
     *
     * @param info
     * @return
     */
    @GetMapping("/getClassRoomData")
    public Result getClassRoomData(Classroom info) {
        //构造分页构造器
        Page<Classroom> pageInfo = new Page<>(info.getPage(), info.getPageSize());
        Page<ClassroomDto> dtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Classroom> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(info.getRoomname() != null, Classroom::getRoomname, info.getRoomname());
        queryWrapper.like(info.getAddress() != null, Classroom::getAddress, info.getAddress());
        queryWrapper.eq(info.getTypeid() != null, Classroom::getTypeid, info.getTypeid());

        //执行分页查询
        classroomService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        //获取查询到的数据中的records
        final List<Classroom> records = pageInfo.getRecords();

        //根据records中的roleid查询角色 封装到userDto中
        final List<ClassroomDto> list = records.stream().map((item) -> {
            ClassroomDto dto = new ClassroomDto();
            BeanUtils.copyProperties(item, dto);

            final Integer typeid = item.getTypeid();

            final Roomtype roomtype = roomtypeService.getById(typeid);

            if (roomtype != null) {
                final String roomname = roomtype.getRoomtype();
                dto.setRoomtype(roomname);
            }
            return dto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return Result.ok(dtoPage);
    }

    /**
     * 添加或者更新用户
     *
     * @param info
     * @return
     */
    @PutMapping("/addOrUpdateClassRoom")
    public Result addOrUpdateClassRoom(@RequestBody Classroom info) {

        final boolean flag = classroomService.saveOrUpdate(info);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }

    /**
     * 删除教室
     *
     * @param info
     * @return
     */
    @DeleteMapping("/delClassRoom")
    public Result delClassRoom(@RequestBody List<Classroom> info) {
        final boolean flag = classroomService.removeBatchByIds(info);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }

    /**
     * 导入
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/excelImport")
    @ResponseBody
    public Result excelImport(MultipartFile file) throws Exception {
        //首先判断是不是空的文件
        if (!file.isEmpty()) {
            //对文文件的全名进行截取然后在后缀名进行删选。
            int begin = file.getOriginalFilename().indexOf(".");
            int last = file.getOriginalFilename().length();
            //获得文件后缀名
            String a = file.getOriginalFilename().substring(begin, last);
            if (!(a.endsWith(".xlsx") || a.endsWith(".xls"))) {
                return Result.fail("文件类型只能为xlsx、xls！");
            }
        } else {
            return Result.fail("文件不能为空！");
        }

        EasyExcel.read(file.getInputStream(), ClassroomDto.class,new ClassroomDtoListener(classroomService,roomtypeService)).sheet().doRead();

        return Result.ok();
    }


    /**
     * 查询教室地址下拉
     * @param
     * @return
     */
    @GetMapping("/getAddressData")
    public Result getAddressData() {

        QueryWrapper<Classroom> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("address")
                .groupBy("address");

        final List<Classroom> list = classroomService.list(queryWrapper);

        return Result.ok(list);
    }


}
