package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import com.itheima.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    EmpService empService;
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp ){
        log.info("员工登录：{}",emp);
        Emp e = empService.login(emp);
        //登陆成功，生成令牌，下发令牌
        if(e != null){
            Map<String , Object> claims = new HashMap<>();
            // 将用户姓名，密码，姓名，放入claims。
            claims.put("username" , e.getUsername());
            claims.put("password" ,e.getPassword());
            claims.put("name" , e.getName());
          //创建Jwt，包含员工信息
            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(jwt);
        }
        //登录失败，返回错误信息
        return  Result.error("密码或账号错误");
    }
}
