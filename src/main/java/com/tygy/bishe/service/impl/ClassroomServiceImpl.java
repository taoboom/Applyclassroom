package com.tygy.bishe.service.impl;

import com.tygy.bishe.dto.ClassroomDto;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.mapper.ClassroomMapper;
import com.tygy.bishe.service.IClassroomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-04-01
 */
@Service
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom> implements IClassroomService {

}
