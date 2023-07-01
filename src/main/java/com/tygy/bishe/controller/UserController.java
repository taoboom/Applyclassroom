package com.tygy.bishe.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.media.jfxmedia.logging.Logger;
import com.tygy.bishe.dto.Result;
import com.tygy.bishe.dto.RoomuseDto;
import com.tygy.bishe.dto.UserDto;
import com.tygy.bishe.entity.Role;
import com.tygy.bishe.entity.User;
import com.tygy.bishe.listener.RoomuseDtoListener;
import com.tygy.bishe.listener.UserDtoListener;
import com.tygy.bishe.service.IRoleService;
import com.tygy.bishe.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.tygy.bishe.tool.MD5Utils.encrypt;
import static com.tygy.bishe.tool.RedisConstants.LOGIN_USER_KEY;
import static com.tygy.bishe.tool.RedisConstants.LOGIN_USER_TTL;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-03-25
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    private Result Login(@RequestBody User info, HttpSession session) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sno", info.getSno());
        queryWrapper.eq("password", encrypt(info.getPassword()));
        queryWrapper.eq("roleid", info.getRoleid());
        final User one = userService.getOne(queryWrapper);

        UserDto userDto = new UserDto();
        if (one == null) {
            return Result.fail("用户名或密码错误！");
        }

        BeanUtils.copyProperties(one,userDto,"password");
        final Role role = roleService.getById(info.getRoleid());
        userDto.setRole(role.getRole());
        //随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);

        //将user对象转为JSON存储
        final String  userToStr = JSONUtil.toJsonStr(userDto);

        //存入redis
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY + token,userToStr);
        //设置有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.MINUTES);

        Map map = new HashMap();
        map.put("token",token);
        map.put("user", userDto);

        return Result.ok(map);
    }

    /**
     * 退出功能
     * @return
     */
    @PostMapping("/loginOut")
    public Result logout(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            return Result.fail("退出失败！");
        }
        final Boolean flag = stringRedisTemplate.delete(LOGIN_USER_KEY + token);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }

    /**
     * 修改密码
     * @param info
     * @return
     */
    @PutMapping("/modifyPassword")
    public Result modifyPassword(@RequestBody UserDto info) {

        final User old = userService.getById(info.getId());

        if (!old.getPassword().equals(encrypt(info.getPassword()))) {
            return Result.fail("旧密码不正确！");
        }
        User user = new User();
        user.setId(info.getId());
        user.setPassword(encrypt(info.getNewPassword()));
        final boolean flag = userService.updateById(user);

        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }


    /**
     * 根据条件查询用户
     * @param info
     * @return
     */
    @GetMapping("/getUserData")
    public Result getUserData(User info) {
        //构造分页构造器
        Page<User> pageInfo = new Page<>(info.getPage(), info.getPageSize());
        Page<UserDto> userDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(info.getSno() != null, User::getSno, info.getSno());
        //不查询密码字段
        queryWrapper.select(User.class, column -> !column.getColumn().equals("password"));

        //执行分页查询
        userService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,userDtoPage,"records");

        //获取查询到的数据中的records
        final List<User> records = pageInfo.getRecords();

        //根据records中的roleid查询角色 封装到userDto中
        final List<UserDto> list = records.stream().map((item) -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(item, userDto);

            final Integer roleid = item.getRoleid();

            final Role role = roleService.getById(roleid);

            if (role != null) {
                final String roleName = role.getRole();
                userDto.setRole(roleName);
            }
            return userDto;
        }).collect(Collectors.toList());

        userDtoPage.setRecords(list);

        return Result.ok(userDtoPage);
    }

    /**
     * 添加或者更新用户
     * @param info
     * @return
     */
    @PutMapping("/addOrUpdateUser")
    public Result addOrUpdateUser(@RequestBody User info) {

        if (info.getPassword() != null&&!"".equals(info.getPassword())) {
            info.setPassword(encrypt(info.getPassword()));
        }

        final boolean flag = userService.saveOrUpdate(info);
        if (!flag) {
            return Result.fail(flag);
        }
        return Result.ok(flag);
    }

    /**
     * 删除用户
     * @param info
     * @return
     */
    @DeleteMapping("/delUser")
    public Result delUser(@RequestBody User info) {
        final boolean flag = userService.removeById(info);
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

        EasyExcel.read(file.getInputStream(), UserDto.class,new UserDtoListener(userService,roleService)).sheet().doRead();

        return Result.ok();
    }

}
