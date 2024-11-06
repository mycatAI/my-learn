package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageBean;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    EmpMapper empMapper;
    /**
     * 查询员工数据
     */
    @Override
    public PageBean selectAll(Integer page, Integer pageSize , String name , Short gender , LocalDate begin , LocalDate end) {
        //1.设置分页参数
        PageHelper.startPage(page,pageSize);
        //2.执行查询
        List<Emp> list = empMapper.selectAll(name ,gender , begin ,end);
        //3.封装pagebean对象
        Page<Emp> p = (Page<Emp>)list;
        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());
        return pageBean;
    }
    public void deleteId(List<Integer> ids){
        //1.调用mapper接口删除
        empMapper.deleteId(ids);
    }
    public void addEmp(Emp emp){
        //1.补充数据
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        //2.调用mapper接口方法
        empMapper.add(emp);
    }
}
