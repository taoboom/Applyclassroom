package com.tygy.bishe.service.impl;

import com.tygy.bishe.dto.RoomuseDto;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.entity.Roomuse;
import com.tygy.bishe.mapper.RoomuseMapper;
import com.tygy.bishe.service.IRoomuseService;
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
 * @since 2023-04-04
 */
@Service
public class RoomuseServiceImpl extends ServiceImpl<RoomuseMapper, Roomuse> implements IRoomuseService {

    @Autowired
    private RoomuseMapper roomuseMapper;


    @Override
    public Map<String, Object> getManageData(RoomuseDto info) {

        List<RoomuseDto> list =roomuseMapper.getManageData(info);
        Integer total = roomuseMapper.getTotalByCondition(info);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }
}
