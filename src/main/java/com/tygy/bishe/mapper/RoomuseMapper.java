package com.tygy.bishe.mapper;

import com.tygy.bishe.common.BaseSqlMapper;
import com.tygy.bishe.dto.RoomuseDto;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.entity.Roomuse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qinyuxiang
 * @since 2023-04-04
 */
public interface RoomuseMapper extends BaseSqlMapper<Roomuse> {


    List<RoomuseDto> getManageData(RoomuseDto info);

    Integer getTotalByCondition(RoomuseDto info);
}
