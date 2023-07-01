package com.tygy.bishe.service;

import com.tygy.bishe.dto.RoomuseDto;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.entity.Roomuse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-04-04
 */
public interface IRoomuseService extends IService<Roomuse> {


    Map<String, Object> getManageData(RoomuseDto info);
}
