package com.itheima.service;

import com.itheima.mapper.DeptMapper;
import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 部门管理
 */
public interface DeptService {
    /**
     * 查询全部部门数据
     */
    List<Dept> list();
    /**
     * 根据部id删除部门
     */
     void deleteId(Integer id);
    /**
     * 添加部门
     */
     void add(Dept dept);
    /**
     *修改部门
     */
    void update(Dept depr);
    /**
     *
     */
}
