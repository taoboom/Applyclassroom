package com.tygy.bishe.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tygy.bishe.dto.Result;
import com.tygy.bishe.dto.RoomuseDto;
import com.tygy.bishe.dto.UserDto;
import com.tygy.bishe.entity.Role;
import com.tygy.bishe.entity.User;
import com.tygy.bishe.listener.UserDtoListener;
import com.tygy.bishe.service.IRoleService;
import com.tygy.bishe.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
@RequestMapping("/upload")
public class UploadController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Value("${files.upload.mapperPath}")
    private String mapperPath;

    //根据时间创建文件夹
    Calendar date=Calendar.getInstance();
    SimpleDateFormat format=new SimpleDateFormat( "yyyy-MM-dd");
    String folderName=format.format(date.getTime());

    @PostMapping("/uploadPic")
    public Result uploadPic(@RequestParam("beforefiles") MultipartFile[] beforeMultipartFiles,@RequestParam(value = "afterfiles",required = false) MultipartFile[] afterMultipartFiles) throws IOException {

        List<String> before_fileName = new ArrayList<>();
        List<String> after_fileName = new ArrayList<>();

        if (beforeMultipartFiles!=null&&beforeMultipartFiles.length > 0) {
            for (MultipartFile beforeFile : beforeMultipartFiles) {
                //给文件名加上时间戳 避免重复
                String fileName = System.currentTimeMillis() + beforeFile.getOriginalFilename();
                // 保存到硬盘
                File dest = new File(fileUploadPath + folderName+"/before/" + fileName);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                //复制图片到该路径下
                beforeFile.transferTo(dest);

                before_fileName.add(mapperPath + folderName + "/before/" + fileName);
            }
        }

        if (afterMultipartFiles!=null&&afterMultipartFiles.length > 0) {
            for (MultipartFile afterFile : afterMultipartFiles) {
                //给文件名加上时间戳 避免重复
                String fileName = System.currentTimeMillis() + afterFile.getOriginalFilename();
                // 保存到硬盘
                File dest = new File(fileUploadPath + folderName+"/after/" + fileName);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                //复制图片到该路径下
                afterFile.transferTo(dest);
                after_fileName.add(mapperPath + folderName + "/after/" + fileName);
            }
        }

        Map<String,List> map = new HashMap<>();
        map.put("before", before_fileName);
        map.put("after", after_fileName);

        return Result.ok(map);
    }


    /**
     * 删除图片
     * @param
     * @return
     * @throws MalformedURLException
     */
    @PostMapping("/delUploadFile")
    public Result delUploadFile(@RequestBody RoomuseDto info) throws MalformedURLException {


        for (String s : info.getBeforeusePic()) {
            final URL url = new URL(s);
            final File file = new File(fileUploadPath + url.getFile().substring(5));
            if (file.exists()) {
                file.delete();
            }
        }
        for (String s : info.getAfterusePic()) {
            final URL url = new URL(s);
            final File file = new File(fileUploadPath + url.getFile().substring(5));
            if (file.exists()) {
                file.delete();
            }
        }

        return Result.ok("删除成功");


    }

}
