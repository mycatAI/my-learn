package com.itheima.mapper;

import com.itheima.pojo.Emp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
}
