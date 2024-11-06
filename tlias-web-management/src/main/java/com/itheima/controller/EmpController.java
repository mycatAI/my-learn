package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工管理Controller
 */
@RequestMapping("/emps")
@Slf4j
@RestController
public class EmpController {
    /**
     * 查询所有员工
     * @param page
     * @param PageSize
     * @return
     */
    @Autowired
    EmpService empService ;
    @GetMapping
    public Result selectAll(@RequestParam(defaultValue = "1") Integer page ,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            String name ,Short gender ,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        //调用service对象
        PageBean pageBean = empService.selectAll(page , pageSize,name ,gender ,begin , end);
        //响应
        return Result.success(pageBean);
    }
    @DeleteMapping("/{ids}")
    public Result deleteId( @PathVariable List<Integer> ids){
        //1.调用service接口批量删除
        empService.deleteId(ids);
        return Result.success();
    }
    @PostMapping
    public Result add(@RequestBody Emp emp){
        //1,使用service接口方法
        empService.addEmp(emp);
        return Result.success();
    }

}
