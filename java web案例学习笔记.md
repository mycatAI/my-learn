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

#### 添加员工：

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

