package com.tygy.bishe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tygy.bishe.entity.User;
import com.tygy.bishe.mapper.UserMapper;
import com.tygy.bishe.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-03-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {



}
