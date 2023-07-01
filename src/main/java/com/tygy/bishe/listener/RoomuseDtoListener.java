package com.tygy.bishe.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tygy.bishe.dto.ClassroomDto;
import com.tygy.bishe.dto.RoomuseDto;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.entity.Roomtype;
import com.tygy.bishe.entity.Roomuse;
import com.tygy.bishe.mapper.ClassroomMapper;
import com.tygy.bishe.mapper.RoomuseMapper;
import com.tygy.bishe.service.IClassroomService;
import com.tygy.bishe.service.IRoomtypeService;
import com.tygy.bishe.service.IRoomuseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomuseDtoListener extends AnalysisEventListener<RoomuseDto> {

    @Autowired
    private IRoomuseService roomuseService;

    @Autowired
    private IClassroomService classroomService;

    @Autowired
    private RoomuseMapper roomuseMapper;


    private List<Roomuse> roomuses = new ArrayList<>();

    public RoomuseDtoListener() {
    }

    public RoomuseDtoListener(IRoomuseService roomuseService, IClassroomService classroomService) {
        this.roomuseService = roomuseService;
        this.classroomService = classroomService;
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
    public void invoke(RoomuseDto roomuseDto, AnalysisContext analysisContext) {

        //根据每条记录的教室名称查询教室表中教室id
        final Classroom info = classroomService.getOne(new QueryWrapper<Classroom>().eq("roomname", roomuseDto.getRoomname()));
        //将查到的id给classroomDto
        final Long id = info.getId();
        roomuseDto.setRoomid(id);
        roomuses.add(roomuseDto);
        if (roomuses.size() >= BATCH_COUNT) {
//            classroomService.saveBatch(classrooms);
            roomuseMapper.insertBatchSomeColumn(roomuses);
            roomuses.clear();
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
      roomuseService.saveBatch(roomuses);
    }
}
