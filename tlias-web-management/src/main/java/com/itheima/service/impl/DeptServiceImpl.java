package com.itheima.service.impl;

import com.itheima.mapper.DeptMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Dept;
import com.itheima.pojo.DeptLog;
import com.itheima.service.DeptLogService;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private EmpMapper empMapper;
    @Override
    public List<Dept> list() {
        return deptMapper.list();
    }
    @Autowired
private DeptLogService deptLogService;
    /**
     * 删除操作
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)//当前方法交给spring事务管理
    @Override
    public void deleteId(Integer id) {
        try {
            deptMapper.deleteId(id);
            int a = 1 / 0;
            //根据部门id删除部门员工；
            empMapper.deleteByDeptId(id);
        }finally {
            DeptLog deptLog = new DeptLog();
            deptLog.setDescription("执行了解散部门操作，此次解散部门为"+id+"部门");
            deptLog.setCreateTime(LocalDateTime.now());
            deptLogService.insert(deptLog);
        }
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
