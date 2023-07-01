package com.tygy.bishe.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tygy.bishe.dto.ClassroomDto;
import com.tygy.bishe.dto.Result;
import com.tygy.bishe.dto.RoomuseDto;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.entity.Roomtype;
import com.tygy.bishe.entity.Roomuse;
import com.tygy.bishe.listener.ClassroomDtoListener;
import com.tygy.bishe.listener.RoomuseDtoListener;
import com.tygy.bishe.service.IClassroomService;
import com.tygy.bishe.service.IRoomuseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-04-04
 */
@RestController
@RequestMapping("/roomuse")
public class RoomuseController {


    @Autowired
    private IRoomuseService roomuseService;

    @Autowired
    private IClassroomService classroomService;

    /**
     * 根据条件查询教室使用情况
     *
     * @param info
     * @return
     */
    @GetMapping("/getRoomUseData")
    public Result getRoomUseData(RoomuseDto info) {

        //根据教室名称查询教室id
        Roomuse roomuse = new Roomuse();
        if (info.getRoomname() != null && !"".equals(info.getRoomname())) {
            LambdaQueryWrapper<Classroom> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Classroom::getRoomname, info.getRoomname());
            final Classroom one = classroomService.getOne(queryWrapper);
            if (one!= null) {
                roomuse.setRoomid(one.getId());
            } else {
                return Result.ok(roomuse);
            }
        }

        //构造分页构造器
        Page<Roomuse> pageInfo = new Page<>(info.getPage(), info.getPageSize());
        Page<RoomuseDto> dtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Roomuse> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(info.getRoomname() != null&&!"".equals(info.getRoomname()), Roomuse::getRoomid, roomuse.getRoomid());
        queryWrapper.like(info.getTeacher() != null&&!"".equals(info.getTeacher()), Roomuse::getTeacher, info.getTeacher());
        queryWrapper.like(info.getCourse() != null&&!"".equals(info.getCourse()), Roomuse::getCourse, info.getCourse());
        queryWrapper.like(info.getClasses() != null&&!"".equals(info.getClasses()), Roomuse::getClasses, info.getClasses());
        queryWrapper.eq(info.getWeeks() != null, Roomuse::getWeeks, info.getWeeks());
        queryWrapper.eq(info.getWeek() != null, Roomuse::getWeek, info.getWeek());
        queryWrapper.eq(info.getSection() != null, Roomuse::getSection, info.getSection());

        //执行分页查询
        roomuseService.page(pageInfo, queryWrapper);


        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        //获取查询到的数据中的records
        final List<Roomuse> records = pageInfo.getRecords();

        //根据records中的roleid查询角色 封装到userDto中
        final List<RoomuseDto> list = records.stream().map((item) -> {
            RoomuseDto dto = new RoomuseDto();
            BeanUtils.copyProperties(item, dto);

            final Long roomid = item.getRoomid();

            final Classroom classroom = classroomService.getById(roomid);

            if (classroom != null) {
                final String roomname = classroom.getRoomname();
                dto.setRoomname(roomname);
            }
            return dto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return Result.ok(dtoPage);
    }

    /**
     * 更新教室使用情况信息
     *
     * @param info
     * @return
     */
    @PutMapping("/updateRoomUse")
    public Result updateRoomUse(@RequestBody Roomuse info) {

        final boolean flag = roomuseService.updateById(info);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }

    /**
     * 删除教室使用情况
     *
     * @param info
     */
    @DeleteMapping("/delRoomUse")
    public Result delRoomUse(@RequestBody List<Roomuse> info) {
        final boolean flag = roomuseService.removeBatchByIds(info);
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

        EasyExcel.read(file.getInputStream(), RoomuseDto.class,new RoomuseDtoListener(roomuseService,classroomService)).sheet().doRead();

        return Result.ok();
    }


    /**
     * 根据条件查询教室使用情况（楼管端）
     *
     * @param info
     * @return
     */
    @GetMapping("/getManageData")
    public Result getManageData(RoomuseDto info) {

       Map<String,Object> map = roomuseService.getManageData(info);

       return Result.ok(map);
    }

    /**
     * 更新教室使用情况中图片信息
     * @param info
     * @return
     */
    @PutMapping("/updateRoomUsePic")
    public Result updateRoomUsePic(@RequestBody RoomuseDto info) {

        if (info.getAfterusePic() == null||info.getAfterusePic()==null) {
            return Result.fail("请先上传到服务器");
        }

        Roomuse roomuse = new Roomuse();
        roomuse.setId(info.getId());
        roomuse.setBeforeuse(info.getBeforeusePic().toString());
        roomuse.setAfteruse(info.getAfterusePic().toString());

        final boolean flag = roomuseService.updateById(roomuse);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }
}
