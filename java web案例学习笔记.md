# java web学习笔记

## 项目实践：

### 环境搭配：

 准备数据库 。

创建springboot项目，引入对应的起步依赖（web，mysql，mybatis，lombok）

配置文件application.properties中引入mybatis的配置信息 ， 准备对应的实体类

controller ， service （接口）， mapper 三层架构。

准备对应的mapper ，service（接口，实现类，），controller基础结构。

### 开发规范：

案例基于当前最主流的前后端分离模式进行开发。（接口文档）该案例基于Restful接口开发的

REST：表述性状态转换 ， 他是一种软件架构风格。 主要依靠URL定位资源，HTTP动词描述操作

（GET：查询ID， POST：新增用户 ， PUT：修改用户 ， DELETE：删除ID用户）

### 开发流程：

查看页面原型  ， 阅读接口文档 ， 思路分析， 接口开发 ， 接口测试 ， 前后端联调

## 部门管理：

### 查询：

####    查询全部数据 ：

####      流程：

#####   controller：1.接受请求 2.调用service查询部门 3.响应

####             1.接受请求：

​           输出用日志输出  。 使用注解@Slf4j记录日志

​             RequestMapping（value= " " , method = RequestMethond.GET)//指定请求方式  ， 或者使用GetMapping("")指定请求方式。

####                    2.调用service查询部门

####                      3.响应

```java
@Slf4j
@RestController
public class DeptController {
    //调用service接口
    @Autowired
    private DeptService deptService;
    @GetMapping("/depts")
    public Result list(){
        log.info("查询全部数据");
        //调用service查询部门数据
        List<Dept> deptList = deptService.list();
        return Result.success(deptList);
    }
}

```

#### Service：1.调用mapper接口查询

​        1.调用mapper接口查询

```java
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    @Override
    public List<Dept> list() {
        return deptMapper.list();
    }
}
```

#####   Mapper：select* from dept 

```java
@Mapper
public interface DeptMapper {
    /*
      *查询全部部门
     */
    @Select("select * from dept")
     List<Dept> list();
}
```

### 前后端联调：

### 删除：需求：根据id删除部门

####  controller：

 1.获取id @PathVariable 获取对象id

 2.调用service对象

3.响应

```java
 /**@PathVariable 获取json数据流
     * 删除部门
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("删除部门id:{}",id);
        //调用service接口
        deptService.deleteId(id);
       return Result.success();
    }
```

#### service：

1.调用mapper接口

```java

    /**
     * 删除操作
     * @param id
     */
    @Override
    public void deleteId(Integer id) {
        deptMapper.deleteId(id);
    }
```

#### Mapper：

1.操作数据库

```java
     List<Dept> list();
    /**
     * 根据id删除部门
     */
    @Delete("delete from dept where id = #{id} ")
    void deleteId(Integer id);
```

### 新增

#### controler:

1.获取请求  ：获取json数据 ， 使用@RequestBody 注解获取json数据

2.调用service对象

3.响应

```java
    /**
     * 添加部门
     */
@RequestBody //获取json数据流
    @PostMapping
    public Result add(@RequestBody  Dept dept){
        log.info("新增部门：" ,dept);
        //
        deptService.add(dept);
        return  Result.success();
    }
```

#### service：

1.对数据进行添加

2.调用Mapper对象方法。

```java
  /**
     * 添加操作
     * @param dept
     */
    public void add(Dept dept){
        dept.setCreateTime(LocalDateTime.now() );
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.insert(dept);
    }
```

#### MApper:

```java

    /**
     * 添加部门
     * @param dept
     */
    @Insert("insert into dept (name , create_time , update_time) values (#{name} , #{createTime} , #{updateTime})")
    void insert(Dept dept);
```

### 修改部门：

#### controller：

1.获取请求

2.调用service对象

3.响应

```java

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
```

#### service：

1.数据添加

2.调用Mapper对象

```java
  /**
     * 修改操作
     * @param dept
     */
    public void update(Dept dept){
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }
```

#### Mapper：

1.操作数据库：

```java
    /**
     * 修改部门
     */
    @Update("update dept set name = #{name} , update_time = #{updateTime} where id =#{id}")
    void update(Dept dept); 
```

可以在文件开头加入@ResultMapping，可以简化编程注解

一个完整的请求路径，应该是类上的@RequesMapping的value属性+方法上的@RequesMapping的value属性

## 员工管理

   ### 员工查询

 #### 员工分页查询： 

>  mysql语句使用limit语句分页查询
>
> 参数1：起始索引 = （页码-1 ）*记录数
>
> 参数2：查询返回记录数

#### 需求分析：

 前端传回两个参数：页码page ， 页码记录数：pageSize 

后台传回两个参数 ，记录总数total ， 数据列表：List  （封装起来 ， PageBean）

#### controller：

1.接收分页参数page ， pageSize

>@RequestParam(defaultValue = "") // 设置默认值

2.调用service进行分页查询， 获取pagebean

3.响应

```java
  /**
     * 查询所有员工
     * @param page
     * @param PageSize
     * @return
     */
    @Autowired
    EmpService empService ;
    @GetMapping("/emps")
    public Result selectAll(@RequestParam(defaultValue = "1") Integer page ,
                            @RequestParam(defaultValue = "10") Integer pageSize){
        //调用service对象
           PageBean pageBean = empService.selectAll(page , pageSize);
        //响应
        return Result.success(pageBean);
    }
```

#### service ：

1.调用mapper接口查询总记录数 total

2.调用mapper接口获取数据列表 rows

3.封装PageBean对象返回

``` java

    /**
     * 查询员工数据
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageBean selectAll(Integer page, Integer pageSize) {
        //，1.处理传给empservice对象的数据
        Integer start = (page - 1)*pageSize;
        //2.调用mapping对象的方法获取数据
        List<Emp> list = empMapper.selectAll(start , pageSize);
        Long totals = empMapper.count();
        PageBean pageBean = new PageBean(totals , list);
        return pageBean;
    }
```

#### mapper：

mysql语句

``` java
    /**
     * 员工信息
     * @param start
     * @param pageSize
     * @return
     */
    @Select("select * from emp limit #{start} , #{pageSize}")
    List<Emp> selectAll(Integer start , Integer pageSize);
    /**
     * 总人数
     * @return
     */
    @Select("select count(*) from emp")
    Long count();
```

##### 分页插件 PageHelper:

``` xml
<!-- PageHelper 分页插件-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.4.7</version>
        </dependency>
```

``` JAVA

    /**
     * 查询员工数据
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageBean selectAll(Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page,pageSize);
        //2.执行查询
        List<Emp> list = empMapper.selectAll();
        //3.封装pagebean对象
        Page<Emp> p = (Page<Emp>)list;
        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());
        return pageBean;
    }
```

>  Columns: count(0)
>  Preparing: select * from emp LIMIT ?  
>
> pagehelper帮助我生成的

#### 员工条件查询：

在员工查询上添加：动态sq-xml映射文件

##### controler：

1.获取请求 ， 与数据

2.规定日期类型的格式，使用@DateTimeFormat 规定日期格式

3.调用service对象，进行数据处理

4.响应

``` java
    @GetMapping("/emps")
    public Result selectAll(@RequestParam(defaultValue = "1") Integer page ,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            String name ,Short gender ,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,////使用@DateTimeFormat 规定日期格式
                            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        //调用service对象
        PageBean pageBean = empService.selectAll(page , pageSize,name ,gender ,begin , end);
        //响应
        return Result.success(pageBean);
    }
```

#### service：

1.设置分页参数

2.执行查询

3.封装PageBEan

``` java
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
```



#### Mapper：

1.动态sql ，执行sql语句

``` xml
//动态sql
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.itheima.mapper.EmpMapper">
    <select id="selectAll" resultType="com.itheima.pojo.Emp">
        <!--条件查询-->
            select *
            from emp
            <where>
                <if test="name != null and name != ''">
                    name like concat('%',#{name},'%')
                </if>
                <if test="gender != null">
                    and gender = #{gender}
                </if>
                <if test="begin != null and end != null">
                    and entrydate between #{begin} and #{end}
                </if>
            </where>
            order by update_time desc
    </select>
</mapper>
                    
```

### 删除员工：

批量删除：使用传入数组

### controler：

@DeleteMapping

1.接受参数id数组 （@PathVariable）

2.调用service进行批量删除

3.响应

``` java
  @DeleteMapping("/{ids}")
    public Result deleteId( @PathVariable List<Integer> ids){
        //1.调用service接口批量删除
        empService.deleteId(ids);
        return Result.success();
    }
```



### service：

调用mapper接口进行批量删除操作

``` java
   public void deleteId(List<Integer> ids){
        //1.调用mapper接口删除
        empMapper.deleteId(ids);
    }
```



#### mapper：

执行sql语句（遍历数组）<foreach>动态遍历

``` xml
<!-- foreach 遍历
 collection ：遍历的集合
 item：遍历的元素
separator：分隔符
open：开头元素
close：结束元素
-->
<delete id="deleteId" >
        <!-- 条件删除-->
        delete from emp where id in
        <foreach collection="ids" item ="id" separator="," open="(" close=")">
            #{id}
        </foreach>
    </delete>
```

### 添加员工：

#### controller：

1.接收参数 ，@RequestBody接受并封装数据

2.调用service接口保存数据

3.响应

``` java
  @PostMapping
    public Result add(@RequestBody Emp emp){
        //1,使用service接口方法
        empService.addEmp(emp);
        return Result.success();
    }

```



#### service：

1.补充实体的基础属性

2。调用mapper方法保存数据

``` java
    public void addEmp(Emp emp){
        //1.补充数据
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        //2.调用mapper接口方法
        empMapper.add(emp);
    }
```



#### Mapper：

1.sql操作

``` java
 @Insert("INSERT INTO emp (username , name ,gender ,image ,job,entrydate ,dept_id , create_time ,update_time)"+
     "values (#{username} , #{name} , #{gender} , #{image} , #{job} ,#{entrydate} ,#{deptId} ,#{createTime},#{updateTime})")
     void add(Emp emp);
```

### 根据id修改：

####    controller：

1.接受参数（@RrquestBody）

2.调用service接口方法

3.响应

``` java
   @PutMapping
    public Result update(@RequestBody Emp emp){
        //调用service对象的接口
        empService.updateId(emp);
        return Result.success();
    }
```

### service：

1。完善基础数据

2.调用mapper接口方法

``` java 
 @Override
    public void updateId(Emp emp) {
        /**
         * 完善基础数据
         */
        //1.完善数据
        emp.setUpdateTime(LocalDateTime.now());
        //2.调用mapper接口的方法
        empMapper.updateId(emp);
    }
```



### mapper:

1.调用数据

``` xml
  <update id="updateId">
        update  emp
        <set>
            <if test="username != null and username != ''"> username = #{username}, </if>
            <if test="password != null and password != ''"> password = #{password}, </if>
            <if test="name != null and name != ''"> name = #{name}, </if>
            <if test="gender != null"> gender = #{gender}, </if>
            <if test="image != null and image != ''"> image = #{image}, </if>
            <if test="job != null"> job = #{job}, </if>
            <if test="entrydate != null"> entrydate = #{entrydate}, </if>
            <if test="deptId != null"> dept_id = #{deptId}, </if>
            <if test="updateTime != null"> update_time = #{updateTime} </if>
        </set>
        where id =# {id}
    </update>
```



## 文件上传：

### 简介：

文件上传是指将本地图片 ， 视频， 音频等文件上传到服务器，供其他用户浏览下载的过程

文件上传在项目应用中非常广泛，我们常发微博，，发微信朋友圈都用到了文件上传功能

### 前端页面三要素：

页面设置

表单项：file         方式：Post         指定表单格式：enctype="multipart/from-data"

``` xml
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>上传文件</title>
</head>
<body>

    <form action="/upload" method="post" enctype="multipart/form-data">
        姓名: <input type="text" name="username"><br>
        年龄: <input type="text" name="age"><br>
        头像: <input type="file" name="image"><br>
        <input type="submit" value="提交">
    </form>
</body>
</html>
```

### 服务器端接受文件：

``` java
@Slf4j
@RestController
public class UploadController {
    @PostMapping("/upload")
    public Result upload(String username , Integer age , MultipartFile image){
        log.info("文件杀那个穿：{},{},{}" ,username ,age ,image);
        return Result.success();
    }
}
```

### 本地存储：

在服务器端，接收到上传的文件之后，将本地存储在本地服务器磁盘中

要想后面上传的文件不被覆盖，就要上传的文件名不相同，构造唯一的文件名

#### UUTID：（通用唯一标识）

1.获取文件类型：int index = originalFilename.lastIndexof（"."）;

   String exname = originalFilename.substring(index)l

​    UUID.randomUUID.tostring()+exname;

> [!IMPORTANT]
>
> ```java
> // MultipartFile常用方法:
> String getOriginalFilename();//获取原始文件名
> void transferto(File dest); //将接受的文件转存到磁盘文件中
> long getsize();//获取文件大小，单位：字节
> byte[]getByte()；//获取文件内容的字节数数组
>     InputStream getInputStream();//获取接收到的文件内容的输入流
> ```



``` java
@Slf4j
@RestController
public class UploadController {
    @PostMapping("/upload")
    public Result upload(String username , Integer age , MultipartFile image) throws Exception{
        log.info("文件杀那个穿：{},{},{}" ,username ,age ,image);
        //获取原始文件名
       String   originalFilename =image.getOriginalFilename();
       //构造唯一的文件名（不重复）
        int index = originalFilename.lastIndexOf(".");
       String extname = originalFilename.substring(index);
        String newFileName = UUID.randomUUID().toString()+extname;
        //将文件储存在服务器的磁盘目录中：D:\image

        image.transferTo(new File("D:\\image\\"+newFileName));
        return Result.success();
    }
}
```

> [!WARNING]
>
> 会产生内存超过1MB从而报错，默认为1MB
>
> 在spring boot 中，文件上传，默认单个文件允许最大大小为1MB。如果需要上传大文件，进行以下配置
>
> #配置单个文件最大上传大小：
>
> spring.servlet.multipart.max-file-size=10MB
>
> #配置单个请求最大上传大小（一次请求可以上传多个文件）
>
> spring.servlet.multipart.max-repuest-size=100MB

### 阿里云OSS：

​        SDK：软件开发工具包 ，包括依赖，代码示例等

准备工作-》参照SDK-》集成使用

#### Bucket:

存储空间是用户用于存储对象的容器，所有对象都必须隶属于某个空间

获取密钥

### 登录功能：

创造LoginController类，用于登录验证

#### controller：

1.接受并封装参数

2.调用service方法进行登录

3.响应（如果返回类型为bull则登陆失败 ， 否则登陆成功）

``` java
@Slf4j
@RestController
public class LoginController {
    @Autowired
    EmpService empService;
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp ){
        log.info("员工登录：{}",emp);
        Emp e = empService.login(emp);
        return e != null?Result.success() : Result.error("密码或账号错误");//主要
    }
}
```



#### service：

1.调用mapper接口擦查询用户信息

``` java
 public Emp login(Emp emp){
        return empMapper.getUserAndPs(emp);
    }
```



#### mapper：

1.调用信息。

``` java
  /*
    员工的登录，查询员工
     */
    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getUserAndPs(Emp emp);
```



> [!WARNING]
>
> 联调测试：在未登录情况下，我们也可以直接访问部门管理，员工管理等功能。

### 登录校验：

传统的会话技术，JWT令牌 ，过滤器，拦截器

> 涉及两方面：登陆标记和统一拦截。
>
> 登陆标记：用户登录成功后，每一次请求，都可以获取到标记。 
>
> 统一拦截：
>
> 过滤器：filter
>
> 拦截器：interceptor

#### 会话技术：

会话：浏览器于服务器的交流。，一次会话可以包含多次请求和响应

会话跟踪：一种维护浏览器状态的方法，服务器需识别多次请求是否来自同一浏览器，以便在同一次会话的多次请求间共享数据。

会话跟踪方案：

客户端会话跟踪技术：Cookie

服务端会话跟踪技术：Session

令牌技术

会话跟踪技术对比：

#### cookie:

cookie：HTTP请求头包含存储先前通过与所述服务器发送的HTTP cookies set-Cookie头

Set-cookie:HTTP响应头被用于服务器用户代理发送Cookie

> [!NOTE]
>
> 跨域区分三个维度：协议，IP/域名，端口。

#### session：

基于cookie，分布集成开发中Session是失效的

### 令牌技术：

####   JWT令牌：

定义了一种简洁的，自包含的格式，用于在通信双方以JSON数据格式安全的传输信息。由于数字签名的存在，这些信息是可靠的。

##### 组成：

第一部分：Header，记录令牌类型，签名算法等。{“alg“ ：“HS256 ”，”type“：”JWT“ ，}

> [!NOTE]
>
> Base64：是一种基于64个可打印字符来表示二进制数码的编码方式

第二部分：Payload（有效载荷），携带一些自定义信息，默认信息等。例如{"id":"1",username ;"tom"}

第三部分：Signature（签名），防止Token被篡改，确保安全性/将header，payload并加入指定密钥，通过签名算法计算而来

场景：登录认证。

1.登录成功后，生成令牌。

2.后续每个请求，都会携带JWR令牌，系统每次请求处理之前，先校验令牌，通过后在处理

主要生成，检验令牌。

#### 引入依赖：

``` xml
    <!--  JWT依赖 0.9.1版本 -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.1</version>
```

> [!WARNING]
>
> 下面的生成和解析，适合于低版本，高版本别的方法



#### JWT生成：

```java
    /**
     * JWT生成
     */
    @Test
    public void testGenJwt(){
        Map<String , Object> claims = new HashMap<>();
        claims.put("id" ,1);
        claims.put("name","tom");

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,"ysclove")  //签名算法
                .setClaims(claims)//设置自定义（载荷）
                .setExpiration(new Date(System.currentTimeMillis()+3600*1000))//设置有效期1H
                .compact();//
        System.out.println(jwt);
    }
```

JWT解析：

```java
  /**
     * 解析JWT
     */
    @Test
    public void testParseJwt() {
        Claims claims = Jwts.parser()
                .setSigningKey("ysclove")//指定签名秘钥
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoidG9tIiwiaWQiOjEsImV4cCI6MTczMDk5NjQ1OH0.ztOeYqCAJR_x0Eto4r-uJX0vcjaMfV1IkLZRL-N-0oM")//解析令牌
                .getBody();
        System.out.println(claims);
    }
```

#### 大概思路：

令牌生成：登陆成功，生成JWT令牌，并返回给前端

令牌校验：在请求达到服务端后，对令牌进行统一拦截，校验。

#### 登录结合生成令牌：

#####  步骤：

引入JWT令牌操作工具类

登录完成后，调用工具类生成JWT令牌，并返回。

### 过滤器filter：

Filter过滤器，是javaweb三大组件（servlet，Filter，Listener）之一。

过滤器可以把对资源的请求拦截下来，从而实现一些特殊的功能。

过滤器一般完成一些通用的操作，比如：登录校验 ， 统一编码等 ， 敏感字段。

#### Filter快速入门：

定义Filter：定义一个类，实现Filter接口，并重写其所有方法。

2.配置Filter：Filter类上加@WebFilter注解 ， 配置拦截资源的路径，应道引导类上加@ServlerComponentScan开启Servlet组件支持

``` java
/**
  另外建造包，filter
*/

package com.itheima.filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.logging.LogRecord;
@WebFilter(urlPatterns = "/*")//配置
public class DemoFilter implements Filter {
    @Override//初始化方法 ，只调用一次
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("init : 初始化方法进行了");
        //放行
        filterChain.doFilter(servletRequest ,servletResponse);
    }

    @Override//拦截请求之后调用，调用多次
    public void init(FilterConfig filterConfig) throws ServletException {
      System.out.println("拦截到了请求");
    }

    @Override//摧毁方法 ，只调用一次
    public void destroy() {
        System.out.println("destroy : 摧毁方法执行了");
    }
}
```

#### 详解：

``` java
//放行前逻辑
filterChain.doFilter(servletRequest ,servletResponse);
//放行后逻辑

```

> [!IMPORTANT]
>
> 放行后访问对资源，资源访问完成后， 还会回到Filter
>
> 如果回到filter，执行放行后的逻辑。

Filter：

拦截具体路径 （/login）， 目录拦截（/emps/*），拦截所有（/*）

过滤器链：

一个web应用中，可配置多个过滤器 ，这多个过滤器就形成了一个过滤器链。

顺序：按照配置的Filter，优先级是按照过滤器类名的自然排序。

###       登录校验Filter：

#### 流程：

1.获取请求url

2.判断是否是登录请求，

 3.获取请求头token ，

4.解析token

5.放行

``` java
package com.itheima.filter;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.json.JSONObject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest rep = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取请求url
         String url = rep.getRequestURL().toString();
         log.info("请求的url：{}" ,url);
        //2.判断是否是登录请求，
        if(url.contains("login")){
            log.info("登陆操作，放行");
            filterChain.doFilter(servletRequest,servletResponse);
            return ;
        }
       // 3.获取请求头token ，
         String jwt = rep.getHeader("token");
        //判断jwt是否存在
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头为空，返回未登陆信息");
            Result error = Result.error("NOT_LOGIN");
            //手动封装 对象--json
            JSONObject jsonObject = new JSONObject(error);
            String login = jsonObject.toString();
            response.getWriter().write(login);
            return ;
        }
        //4.解析token
        try{
            JwtUtils.parseJWT(jwt);
        }catch(Exception e){
            e.printStackTrace();
            log.info("解析令失败 ， 返回登陆错误信息");
            Result error = Result.error("NOT_LOGIN");
            //手动封装 对象--json
            JSONObject jsonObject = new JSONObject(error);
            String login = jsonObject.toString();
            response.getWriter().write(login);
            return ;
        }
        // 5.放行
        log.info("linpaihef:");
        filterChain.doFilter(servletRequest,servletResponse);
       // 5.放行
    }
}
```



### 拦截器（Interceptor）

 简介：

是一种动态拦截方法调用的机制 ，类似于过滤器。spring框架中提供的，用来动态拦截控制器方法的执行。

作用：拦截请求 ， 在指定的方法调用前后，根据业务需要执行预先定义好的代码。

快速入门：

1.定义拦截器，实现Handerinterceptor接口，重写其方法

2.注册拦截器

prehandle // 目标方法执行前执行，返回true：放行 ， 返回false：不放行

posthandle：//目标资源方法执行后执行

afterCompletion：//试图渲染完毕后执行，最后执行

3.配置拦截器

配置类：webConfig+注解@Configuration

重写方法，addInterceptor

#### 细节：

拦截器可以根据需求，配置不同拦截路径：

addpathPatterns//需要拦截哪些资源

excludePathPatterns //不需要拦截哪些资源

/* 一级路径 ， /**任意级路径

Filter与Interceptor：

接口规范不同：过滤器需要实现Filter接口，而拦截器需要实现HandlerInterceptor接口

拦截规范不同：过滤器Filter会拦截所有的资源，而Interceptor只会拦截Spring环境中的资源。

#### Interceptor登录校验：

1.获取请求

2.判断请求url中是否包含login，如果包含，说明是登录操作，放行。

3.获取请求头中的令牌

4.判断令牌是否存在，如果不存在，返回错误消息

5.解析token，如果解析失败返回错误结果

6.放行。

``` java
package com.itheima.interceptor;

import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component//ioc容器
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override//目标资源方法运行前，返回ture：放行 ，返回false：拦截
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("prehandle");
        //获取url
        String url = request.getRequestURL().toString();
        //2.判断url是否包含login
        if(url.contains("login")){
            log.info("放行操作，登录");
           return true;
        }
        //3.获取请求头令牌
        String jwt = request.getHeader("token");
        //4.判断请求头令牌是否为空
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头为空：返回未登录信息");
            Result error = Result.error("NOT_LOGIN");
            //手动封装
            JSONObject jsonObject = new JSONObject(error);
            String login = jsonObject.toString();
            response.getWriter().write(login);
            return false ;
        }
        // 5.解析jwt令牌
        try{
            JwtUtils.parseJWT(jwt);
        }catch(Exception e){
            e.printStackTrace();
            Result error = Result.error("NOT_LOGIN");
            //手动封装
            JSONObject jsonObject = new JSONObject(error);
            String login = jsonObject.toString();
            response.getWriter().write(login);
            return false ;
        }
        //6.放行
        log.info("放行：");
        return true;
    }

    @Override//目标资源方法运行后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       System.out.println("postHandle");
    }

    @Override//视图渲染完毕后执行，最后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion");
    }
}
```

### 异常处理：

 方案一：在Collection的方法中进行try...catch处理。（繁琐，臃肿）

方案二：全局异常处理器。（推荐）

#### 全局异常处理器：

定义全局异常处理器：添加@RestControllerAdvice

@ExceptionHandler（一种类型异常）

``` java
package com.itheima.exception;

import com.itheima.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    public Result ex(Exception ex){
        ex.printStackTrace();
        return Result.error("对不起，操作失败请联系管理员");
    }
}

```



### Spring事务管理&AOP：

#### 开启事务：@Transacyional

#### 位置：业务（service）层 的方法上，类上，接口上。

``` java
 @Transactional//当前方法交给spring事务管理
    @Override
    public void deleteId(Integer id) {
        deptMapper.deleteId(id);
        int a = 1 /0;
        //根据部门id删除部门员工；
        empMapper.deleteByDeptId(id);
    }
```

#### 作用：

将当前方法交给Spring进行事务管理，方法执行前，开启事务；成功执行完毕，提交事务；出现异常，回滚事务。

#### spring事务管理日志：

```yml
#spring事务管理日志
logging:
  level:
    org.springframework.jdbc.support.JdbcTransactionManager: debug
```

#### 事务进阶：

#####  rollbackfor：

默认情况下，只有出现RuntimeRxception才回滚事务。RollbackFor属性用于控制出现何种异常类型，回滚事务。

``` java
    @Transactional(rollbackFor = Exception.class)//当前方法交给spring事务管理
    @Override
    public void deleteId(Integer id) {
        deptMapper.deleteId(id);
        int a = 1 /0;
        //根据部门id删除部门员工；
        empMapper.deleteByDeptId(id);
    }
```

##### propagation:

事务的传播行为:指的是当一个事务方法被另一个事务方法调用时，这个事务方法应该如何进行事务控制。

加入@propagation属性

| 属性值      | 含义                                         |
| ----------- | -------------------------------------------- |
| ** REQUIRD  | [默认事务]需要事务，有则加入，无则创建新事务** |
| ** REQUIRD_NEW | 需要新事务，无论有无，总是创建新事物         |
| SUPPORTS    | 支持事务，有则加入，无则在无事务状态中进行   |
| NOT_SUPPORTED   | 不支持事务，在无事务状态下运行，如果当前存在已有事务，则挂起当前事务|
| MANDTORY|必须有事务，否则抛出异常|
| NEVER|必须没事务，否则抛出异常|

REQUIRE：大部分情况下都是用该传播行为

REQUIRE_NEW：当我们不希望事务之间相互影响时，可以使用传播行为，比如：下订单前需要记录日志，不论订单保存成功与否，都需要保证日志记录能够记录成功。
