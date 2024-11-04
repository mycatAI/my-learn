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







