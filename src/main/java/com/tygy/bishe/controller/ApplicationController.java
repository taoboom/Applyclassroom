package com.tygy.bishe.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tygy.bishe.dto.*;
import com.tygy.bishe.entity.*;
import com.tygy.bishe.mapper.RoomuseMapper;
import com.tygy.bishe.service.*;
import org.apache.poi.ss.formula.functions.Roman;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-04-17
 */
@RestController
@CrossOrigin
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private IClassroomService classroomService;

    @Autowired
    private IRoomuseService roomuseService;

    @Autowired
    private IApplicationService applicationService;

    @Autowired
    private IRoomtypeService roomtypeService;

    @Autowired
    private IUserService userService;

    /**
     * 根据条件查询空闲教室
     *
     * @param info
     * @return
     */
    @PostMapping("/getEmptyRoomData")
    public Result getEmptyRoomData(@RequestBody EmptyRoomDto info) {


        //构造分页构造器
        Page<Classroom> pageInfo = new Page<>(info.getPage(), info.getPageSize());
        Page<ClassroomDto> dtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Classroom> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(info.getRoomname() != null, Classroom::getRoomname, info.getRoomname());
        queryWrapper.like(info.getAddress() != null, Classroom::getAddress, info.getAddress());
        queryWrapper.eq(info.getTypeid() != null, Classroom::getTypeid, info.getTypeid());

        //子查询条件构造器  查询教室使用表中周、星期、节次已被使用的教室id
        QueryWrapper<Roomuse> roomuseQueryWrapper = new QueryWrapper<>();
        roomuseQueryWrapper.select("DISTINCT roomid")
                .lambda()
                .in(info.getWeeksList() != null && info.getWeeksList().length > 0, Roomuse::getWeeks, info.getWeeksList())
                .in(info.getWeekList() != null && info.getWeekList().length > 0, Roomuse::getWeek, info.getWeekList())
                .in(info.getSectionList() != null && info.getSectionList().length > 0, Roomuse::getSection, info.getSectionList());


        //子查询条件构造器  查询教室使用表中周、星期、节次已被使用的教室id
        QueryWrapper<Application> applicationQueryWrapper = new QueryWrapper<>();
        applicationQueryWrapper.select("DISTINCT roomid")
                .lambda()
                .in(info.getWeeksList() != null && info.getWeeksList().length > 0, Application::getWeeks, info.getWeeksList())
                .in(info.getWeekList() != null && info.getWeekList().length > 0, Application::getWeek, info.getWeekList())
                .in(info.getSectionList() != null && info.getSectionList().length > 0, Application::getSection, info.getSectionList());


        List roomUseList = new ArrayList();
        List applyRoomList = new ArrayList();

        if ((info.getWeeksList() != null && info.getWeeksList().length > 0)
                ||(info.getWeekList() != null && info.getWeekList().length > 0)
                ||(info.getSectionList() != null && info.getSectionList().length > 0)){

            roomUseList = roomuseService.list(roomuseQueryWrapper).stream().map((item) -> {
                return item.getRoomid();
            }).collect(Collectors.toList());
        }

        if ((info.getWeeksList() != null && info.getWeeksList().length > 0)
                ||(info.getWeekList() != null && info.getWeekList().length > 0)
                ||(info.getSectionList() != null && info.getSectionList().length > 0)){

             applyRoomList = applicationService.list(applicationQueryWrapper).stream().map((item) -> {
                return item.getRoomid();
            }).collect(Collectors.toList());
        }



        queryWrapper.notIn(!roomUseList.isEmpty()&&roomUseList.size()>0,Classroom::getId,roomUseList);
        queryWrapper.notIn(!applyRoomList.isEmpty()&&applyRoomList.size()>0,Classroom::getId,applyRoomList);

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
     * 检查当前教室在此时间是否为空
     * @param info
     * @return
     */
    @PostMapping("/checkEmptyData")
    public Result checkEmptyData(@RequestBody EmptyRoomDto info) {

        QueryWrapper<Roomuse> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id")
                .lambda()
                .eq(info.getId() != null, Roomuse::getRoomid, info.getId())
                .eq(info.getWeeks() != null, Roomuse::getWeeks, info.getWeeks())
                .eq(info.getWeek() != null, Roomuse::getWeek, info.getWeek())
                .in(info.getSectionList().length > 0, Roomuse::getSection, info.getSectionList());


        QueryWrapper<Application> applicationQueryWrapper = new QueryWrapper<>();
        applicationQueryWrapper.select("id")
                .lambda()
                .eq(info.getId() != null, Application::getRoomid, info.getId())
                .eq(info.getWeeks() != null, Application::getWeeks, info.getWeeks())
                .eq(info.getWeek() != null, Application::getWeek, info.getWeek())
                .in(info.getSectionList().length > 0, Application::getSection, info.getSectionList());

        final long count = roomuseService.count(queryWrapper);
        final long count1 = applicationService.count(applicationQueryWrapper);

        if (count > 0 || count1 > 0) {
            return Result.fail("此节次不可申请！");
        }


        return Result.ok();
    }

    /**
     * 查询审核人
     * @param
     * @return
     */
    @GetMapping("/getCheckerList")
    public Result getCheckerList() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("roleid", "4");
        final List<User> list = userService.list(queryWrapper);

        return Result.ok(list);
    }

    /**
     * 申请教室
     *
     * @param info
     * @return
     */
    @PostMapping("/applyClassRoom")
    public Result applyClassRoom(@RequestBody ApplicationDto info) {

        Application list = new Application();
        Application list2 = new Application();
        list.setRoomid(info.getId());
        list.setName(info.getName());
        list.setSno(info.getSno());
        list.setWeeks(info.getWeeks());
        list.setWeek(info.getWeek());
        list.setReason(info.getReason());
        list.setChecker(info.getChecker());
        list.setSection(info.getSectionList()[0]);
        list.setState(info.getState());
        list.setClasses(info.getClasses());
        
        BeanUtils.copyProperties(list,list2);
        //如果节次选了两节  需再插入一条
        boolean flag1 = false;
        if (info.getSectionList().length > 1) {
            list2.setSection(info.getSectionList()[1]);
            flag1 = applicationService.save(list2);
        }
        final boolean flag = applicationService.save(list);
        if (!flag||!flag1) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }

    /**
     * 查询申请记录
     * @param info
     * @return
     */
    @GetMapping("/getApplyRecordData")
    public Result getApplyRecordData(Application info) {

        //构造分页构造器
        Page<Application> pageInfo = new Page<>(info.getPage(), info.getPageSize());
        Page<ApplicationDto> dtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Application> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.eq(info.getState() != null, Application::getState, info.getState());
        queryWrapper.eq(info.getChecker() != null, Application::getChecker, info.getChecker());
        queryWrapper.eq(info.getSno() != null, Application::getSno, info.getSno());
        //管理员查询申请记录所用
        queryWrapper.eq(info.getState2() != null, Application::getState2, info.getState2());

        //执行分页查询
        applicationService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        //获取查询到的数据中的records
        final List<Application> records = pageInfo.getRecords();

        //根据records中的roleid查询角色 封装到userDto中
        final List<ApplicationDto> list = records.stream().map((item) -> {
            ApplicationDto dto = new ApplicationDto();
            BeanUtils.copyProperties(item, dto);

            final Long roomid = item.getRoomid();

            final Classroom classroom = classroomService.getById(roomid);

            if (classroom != null) {
                final String roomname = classroom.getRoomname();
                final String address = classroom.getAddress();
                dto.setRoomname(roomname);
                dto.setAddress(address);
            }
            return dto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return Result.ok(dtoPage);
    }


    /**
     * 删除申请
     *
     * @param info
     */
    @DeleteMapping("/delApplyRoom")
    public Result delApplyRoom(@RequestBody Application info) {
        final boolean flag = applicationService.removeById(info);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }

    /**
     * 审核申请
     *
     * @param info
     * @return
     */
    @PostMapping("/checkApply")
    public Result checkApply(@RequestBody Application info) {

        //更新申请表
        final boolean flag = applicationService.updateById(info);

        //如果教务处管理员审核通过向教室使用表插入数据
        boolean flag2=true;
        if (info.getState2() == 1) {
            final Application application = applicationService.getById(info);
            Roomuse roomuse = new Roomuse();
            roomuse.setClasses(application.getClasses());
            roomuse.setRoomid(application.getRoomid());
            roomuse.setApply(application.getName());
            roomuse.setWeeks(application.getWeeks());
            roomuse.setWeek(application.getWeek());
            roomuse.setSection(application.getSection());
            roomuse.setCourse(application.getReason());
            roomuse.setSno(application.getSno());
            flag2 = roomuseService.save(roomuse);
        }

        if (!flag&&!flag2) {
            return Result.fail(flag);
        }

        return Result.ok();
    }



}
