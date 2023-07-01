package com.tygy.bishe.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tygy.bishe.dto.ClassroomDto;
import com.tygy.bishe.entity.Classroom;
import com.tygy.bishe.entity.Roomtype;
import com.tygy.bishe.mapper.ClassroomMapper;
import com.tygy.bishe.service.IClassroomService;
import com.tygy.bishe.service.IRoomtypeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassroomDtoListener extends AnalysisEventListener<ClassroomDto> {

    @Autowired
    private IClassroomService classroomService;

    @Autowired
    private ClassroomMapper classroomMapper;

    @Autowired
    private IRoomtypeService roomtypeService;

    private List<Classroom> classrooms = new ArrayList<>();

    public ClassroomDtoListener() {
    }

    public ClassroomDtoListener(IClassroomService classroomService, IRoomtypeService roomtypeService) {
        this.classroomService = classroomService;
        this.roomtypeService = roomtypeService;
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
    public void invoke(ClassroomDto classroomDto, AnalysisContext analysisContext) {
        //查询当前的教室名称和教室类型表中名称相同的id

        Roomtype info = roomtypeService.getOne(new QueryWrapper<Roomtype>()
       	.eq("roomtype",classroomDto.getRoomtype()));
        //将查到的id给classroomDto
        final Integer id = info.getId();
        classroomDto.setTypeid(id);
        classrooms.add(classroomDto);
        if (classrooms.size() >= BATCH_COUNT) {
//            classroomService.saveBatch(classrooms);
            classroomMapper.insertBatchSomeColumn(classrooms);
            classrooms.clear();
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
      classroomService.saveBatch(classrooms);
    }
}
