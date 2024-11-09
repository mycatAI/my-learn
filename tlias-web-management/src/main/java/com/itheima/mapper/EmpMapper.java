package com.itheima.mapper;

import com.itheima.pojo.Emp;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工管理
 */
@Mapper
public interface EmpMapper {
    /**
     * 员工信息查询
     * @return
     */
    public List<Emp> selectAll(String name, Short gender,LocalDate begin,LocalDate end);
     void  deleteId(List<Integer> ids);
     @Insert("INSERT INTO emp (username , name ,gender ,image ,job,entrydate ,dept_id , create_time ,update_time)"+
     "values (#{username} , #{name} , #{gender} , #{image} , #{job} ,#{entrydate} ,#{deptId} ,#{createTime},#{updateTime})")
     void add(Emp emp);

    void updateId(Emp emp);
    /*
    员工的登录，查询员工
     */
    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getUserAndPs(Emp emp);
    /**
     * 根据部门id删除部门员工
     */
    @Delete("delete  from emp where dept_id = #{deptId}")
    void deleteByDeptId(Integer deptId);
}
