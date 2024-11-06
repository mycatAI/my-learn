package com.itheima.service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.PageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工管理
 */
public interface EmpService {
    /**
     * 查询员工
     */
    PageBean selectAll(Integer page , Integer pageBean , String name , Short gender , LocalDate begin , LocalDate end);
    void deleteId(List<Integer> ids);
    void addEmp(Emp emp);
}
