package com.tygy.bishe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.entity.Roomtype;
import com.tygy.bishe.mapper.ClassroomMapper;
import com.tygy.bishe.mapper.RoomtypeMapper;
import com.tygy.bishe.service.IRoomtypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-03-27
 */
@Service
public class RoomtypeServiceImpl extends ServiceImpl<RoomtypeMapper, Roomtype> implements IRoomtypeService {

}
