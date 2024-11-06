package com.itheima.service.impl;

import com.itheima.mapper.DeptMapper;
import com.itheima.pojo.Dept;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    @Override
    public List<Dept> list() {
        return deptMapper.list();
    }

    /**
     * 删除操作
     * @param id
     */
    @Override
    public void deleteId(Integer id) {
        deptMapper.deleteId(id);
    }

    /**
     * 添加操作
     * @param dept
     */
    public void add(Dept dept){
        dept.setCreateTime(LocalDateTime.now() );
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.insert(dept);
    }

    /**
     * 修改操作
     * @param dept
     */
    public void update(Dept dept){
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }

}
