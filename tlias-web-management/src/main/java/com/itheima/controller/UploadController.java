package com.itheima.controller;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    @PostMapping("/upload")
    public Result upload(String username , Integer age , MultipartFile image) throws Exception{
        log.info("文件杀那个穿：{},{},{}" ,username ,age ,image);
        //获取原始文件名
       String   originalFilename =image.getOriginalFilename();
       //构造唯一的文件名（不重复）
        int index = originalFilename.lastIndexOf(".");
       String extname = originalFilename.substring(index);
        String newFileName = UUID.randomUUID().toString()+extname;
        //将文件储存在服务器的磁盘目录中：D:\image
        image.transferTo(new File("D:\\image\\"+newFileName));
        return Result.success();

    }
}
