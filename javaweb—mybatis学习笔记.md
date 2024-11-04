# javaweb学习笔记（mybatis)部分

## Mybatis:

1.准备工作（创建spring boot工程 ， 数据库表user ， 实体类user；

```java
#配置数据库连接信息。 - 四要素
#驱动类名称
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
#数据库连接的url
spring.datasource.url=jdbc:mysql://localhost:3306/mybatis
#连接数据库的用户名
spring.datasource.username=root
#连接数据库的密码
spring.datasource.password=ysc2003yscysc
    // spring.datasource为数据库连接池。
```

2.引入mybatis的相关依赖，配置mybatis。

   勾选既可以 ， 配置数据库四要素。

3.编写sql语句

```java
@Mapper //在运行时自动生成接口的实现类对象（代理对象），并且该对象交给IOC容器管理
public interface UserMapper {
    //查询全部对象信息
    @Select("select * from user")
    public List<User> list();
}
```

4.单元测试：

```java
    @Autowired //依赖注入
    private UserMapper usermapper ;
    @Test
    public void testListUser(){
    List<User> userlist = usermapper.list();
    userlist.stream().forEach(user->{//字节流输出
        System.out.println(userlist);
    });
}
```

 

## JDBC：

1.注册驱动

2.获取连接对象

3.获取执行sql的对像Statement ， 执行sql ， 返回结果。

4。封装结果数据

```java
 @Test
    public void testJdbc()throws Exception{
    //1.注册驱动
     Class.forName("com.mysql.cj.jdbc.Driver");
     //2.获取链接
     String url = "jdbc:mysql://localhost:3306/mybatis";
     String root = "root" ;
     String password = "ysc2003yscysc";
     Connection connection = DriverManager.getConnection(url ,root , password);
     //3.获取执行操作的对象Statement， 执行sql ， 返回结果
     String sql = "select * from user";
     Statement sta =  connection.createStatement();
     ResultSet resultset = sta.executeQuery(sql);
     //4.封装数据
     List<User> userList = new ArrayList<>();
     while(resultset.next()){
         int id = resultset.getInt("id");
         String name = resultset.getString("name");
         short age = resultset.getShort("age");
         short gender = resultset.getShort("gender");
         String phone = resultset.getString("phone");
         User user = new User(id,name , age , gender , phone);
         userList.add(user);
     }
     //5.释放资源
     sta.close();
     connection.close();
     userList.stream().forEach(user ->{
         System.out.println(user);
     });
 }
```

## 数据库连接池：

连接池是个容器 ， 负责分配 ， 管理数据库连接（Connection）它允许应用程序重复使用一个现有的数据库连接， 而不是在重新建一个释放空闲时间超过最大空闲时间的连接， 来避免因为没有释放连接而引起的数据库连接遗漏。

获取连接：

```java
使用标准接口 // DataSource
Connection getConnection ()throws SQLExpection; 

```

## lombok：引入lombok依赖

lombok通过注解的形式自动生成构造器 ， 等方法， 并自动生成日志变量， 简化java开发，提高效率

@gerrer/@Setter   为所有属性提供get/set方法

@toString               会给类自动生成易阅读的tostring方法

@EqualsAndHashcode   提供类所拥有的非静态字段重写equals方法和hashcode方法。

@Data                              提供了更综合的生成代码功能。

@NoArgsConstructor       为实体类生成无参的构造器方法

@AllArgsConstructor    我i实体类提供除了static修饰的字段之外带有参数的构造方法。

## Mybatis基础操作：

###    准备工作：准备数据库表emp

### 删除：参数占位符：#{};

###  预编译SQL：==>  Preparing: delete from emp where id = ?  

优势： 性能更高 ， 更安全（预防预输入）

参数占位符：

#### #{}：执行SQL时，会将#{}替换为？，生成预编译SQL，会自动设置参数 ， 

使用时机：参数传递 ， 都将使用#{}

#### ￥{}：直接将参数拼接在SQL语句中 ， 存在SQL注入问题

使用时机：如果对表名 ， 列表进行动态设置时使用

## mybatis增加数据：

```java
 @Insert("insert into emp(username, name, gender, image, job, entrydate, dept_id, create_time, update_time)" +
            "values (#{username} , #{name} ,#{gender} ,#{image} ,#{job} ,#{entrydate}  ,#{deptId} ,#{createTime},#{updateTime})")
    public void insert(emp emp);
//如果参数过多可以使用emp类型传参数

```

###  新增（主键返回）：

在添加数据成功后 ， 需要获取插入数据数据的主键。

```java
//实现 加入@Options
@Options(KeyProperty = "id" , useGeneratedkeys = true)//获取主键值 ， 封装给emp的id
 @Insert("insert into emp(username, name, gender, image, job, entrydate, dept_id, create_time, update_time)" +
            "values (#{username} , #{name} ,#{gender} ,#{image} ,#{job} ,#{entrydate}  ,#{deptId} ,#{createTime},#{updateTime})")

```

##### mybatis更新：

```java
 //接口端：
@Update("update emp set username = #{username} , name = #{name} , gender = #{gender} , image = #{image} ,job =#{job}," +
            "entrydate = #{entrydate},dept_id=#{deptId},update_time =#{updateTime}where id =#{id} ")
    public void update(emp emp);
//测试端：
    @Test
    public void testUpdate(){
        emp emp = new emp();
        emp.setId((18));
        emp.setUsername("tom5");
        emp.setName("汤姆5");
        emp.setImage("1.jpg");
        emp.setGender((short)1);
        emp.setJob((short)1);
        emp.setEntrydate(LocalDate.of(2000 ,01,01));
        emp.setUpdateTime(LocalDateTime.now());
        emp.setDeptId(1);
        //执行更新员工操作
        empmapper.update(emp);
    }
```

## mybatis查询：（根据id）

```java
   
@Select("select id , username , password ,name , gender ,image , job ,entrydate , dept_id deprId" +
            ",create_time createTime ,update_time updateTime  from emp where id = #{id}")
出现null值解决
    方案一：起别名
    方案二：通过@Results ，@Result注解手动映射封装。
    方案三：开启mybatis的驼峰命名自动映射开关
    #开启mybatis 的驼峰命名自动映射开关
mybatis.configuration.map-underscore-to-camel-case=true 
    concat 字符串拼接函数
    select concat('hello' , 'mysql' , 'world');
```

## XML映射文件：

XML映射文件的名称与Mapper接口名称一致，并且将XML隐射文件和Mapper接口放置在相同包下（同包同名）

XML映射文件的namespace属性为Mapper接口全限定名一致

XML隐射文件中的SQL文件的ID与mapper接口中的方法名一致，并保持返回类型一致。

## 动态SQL:

随着用户的输入或外部输出条件的变化而变化的SQL语句 ，我们称之为动态SQL

### <if>条件晒选:

```xml
//动态查询
<select>
 select * 
    from emp
  where    //容易让语句发生错误
    <where>// 可以自动去除and或or
<if test ="name != null"
name like concat('%',#{name} ,'%')
</if>
<if test = "gender != null">
    and gender = #{gender}
</if> 
<if  test = "begin != null and end != null">
        and entrydate between #{begin} and #{end}    
</if>
        </where>
order by update_time desc
</select>
//动态更新
   <update id="update2">
        update emp
        <set>  //会自动去除逗号
        <if test="username != null">username = #{username},</if>
        <if test="name != null ">name = #{name},</if>
        <if test="gender != null">gender = #{gender},</if>
        <if test="image != null">image = #{image} ,</if>
        <if test="job != null">job = #{job},</if>
        <if test ="entrydate != null">entrydate = #{entrydate},</if>
        <if test = "deptId != null">dept_id=#{deptId},</if>
        <if test = "updateTime != null"> update_time =#{updateTime}</if>
        </set>
        where id = #{id};
    </update>
```

### <foreach> 循环遍历：

```xml
<!-- 
collection:遍历的集合
item：遍历出来的元素
separator ：分隔符
open：遍历开始前拼接的sql片段
close：遍历结束后拼接的片段
-->

<foreach collection = "" item = "id" separator = "," open = "" close = "">
</foreach>
```

<sql><include>

```xml
  <sql id="comment">//id 为sql名
        select id , username ,password ,name , gender ,job ,entrydate ,dept_id , create_time , update_time
        from emp
    </sql>
<include refid = "">//refid调用的sql名
    
</include>
```

