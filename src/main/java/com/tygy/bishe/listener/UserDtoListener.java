package com.tygy.bishe.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tygy.bishe.dto.RoomuseDto;
import com.tygy.bishe.dto.UserDto;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.entity.Role;
import com.tygy.bishe.entity.Roomuse;
import com.tygy.bishe.entity.User;
import com.tygy.bishe.mapper.RoomuseMapper;
import com.tygy.bishe.mapper.UserMapper;
import com.tygy.bishe.service.IClassroomService;
import com.tygy.bishe.service.IRoleService;
import com.tygy.bishe.service.IRoomuseService;
import com.tygy.bishe.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tygy.bishe.tool.MD5Utils.encrypt;

public class UserDtoListener extends AnalysisEventListener<UserDto> {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private UserMapper userMapper;


    private List<User> users = new ArrayList<>();

    public UserDtoListener() {
    }

    public UserDtoListener(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    
     /**
     * 批处理阈值2000
     */
    private static final int BATCH_COUNT = 2000;

    /**
    *一行行读取excel内容，然后用MybatisPlus的方法进行导入
    *数据每到达2000条时，进行一次存储
    */
    @Override
    public void invoke(UserDto userDto, AnalysisContext analysisContext) {

        //根据每条记录的角色名称查询角色表中角色id
        final Role info = roleService.getOne(new QueryWrapper<Role>().eq("role", userDto.getRole()));
        //将查到的id给classroomDto
        final Integer id = info.getId();
        userDto.setRoleid(id);
        userDto.setPassword(encrypt("123456"));
        users.add(userDto);
        if (users.size() >= BATCH_COUNT) {
//            classroomService.saveBatch(classrooms);
            userMapper.insertBatchSomeColumn(users);
            users.clear();
        }

    }

    //  读取表头内容，导出可用到
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头：" + headMap);
    }

    //  读取完成之后进行的操作
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
      userService.saveBatch(users);
    }
}
