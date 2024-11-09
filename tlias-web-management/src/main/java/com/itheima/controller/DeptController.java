package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理Controller
 */
@Slf4j
@RestController
@RequestMapping("depts")
public class DeptController {
    //调用service接口
    /**
    *查询部门数据
     */
    @Autowired
    private DeptService deptService;
    @GetMapping
    public Result list(){
        log.info("查询全部数据");
        //调用service查询部门数据
        List<Dept> deptList = deptService.list();
        return Result.success(deptList);
    }
    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("删除部门id:{}",id);
        //调用service接口
        deptService.deleteId(id);//根据id删除部门
        //根据部门id删除员工

       return Result.success();
    }
    /**
     * 添加部门
     */
    @PostMapping
    public Result add(@RequestBody  Dept dept){
        log.info("新增部门：" ,dept);
        //
        deptService.add(dept);
        return  Result.success();
    }

    /**
     * 修改部门
     * @param dept
     * @return
     */
    @PutMapping
    public Result update(@RequestBody Dept dept){
        log.info("修改部门：",dept);
        //调用service
        deptService.update(dept);
        return Result.success();
    }
    /**
     * 员工分页查询
     */

}
