package com.itheima.mapper;

import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 部门管理
 */
@Mapper
public interface DeptMapper {
    /*
      *查询全部部门
     */
    @Select("select * from dept")
     List<Dept> list();
    /**
     * 根据id删除部门
     */
    @Delete("delete from dept where id = #{id} ")
    void deleteId(Integer id);

    /**
     * 添加部门
     * @param dept
     */
    @Insert("insert into dept (name , create_time , update_time) values (#{name} , #{createTime} , #{updateTime})")
    void insert(Dept dept);
    /**
     * 修改部门
     */
    @Update("update dept set name = #{name} , update_time = #{updateTime} where id =#{id}")
    void update(Dept dept);
}
