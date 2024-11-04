---

---

# JAVAweb的十天学习

我的数据库是：8.0.39版本。

DDL：定义数据库对象（数据库 ， 表， 字段）

DML：数据操作语言，用来对数据表中的数据进行删改。

DQL：数据查询语言 ，用来查询数据数据库中表的记录。

DCL：数据控制语言，用来创造数据库用户 ， 控制数据库的权限。

## DDL：查询 ，使用， 创建 ，删除

```mysql
 查询：show database s ；

创建：create  database  if  not exists 数据库名字 ；

 使用：use 数据库名字 ， 查询使用的数据库：select database（）；

 删除数据库：drop database  if exists 数据库名字；
 //关于表结构
 -- 创建表结构
 create table 表名 (
   字段1 ， 字段类型 ，[约束][comment 字段1注释] ，
     .....
     字段n ， 字段类型 ， [约束][comment 字段n注释]
 )
 约束：约束作用在表中字段上的规则 ， 用于限制存储在表中的数据。
 目的：保证在数据库中的数据的正确性 ， 有效性， 完整性。
 -- 非空约束  not null ； -- 唯一约束  unique   -- 主键约束 primary key auto_increment  自动增长
 -- 默认约束 default      -- 外键约束  foreign key ;
 从查询当前数据库所有的表 ：show tables ;
  查询表结构 ：desc 表名 ；
  查询建表语句 show create table 表名 ；
  修改表结构：
 添加字段 ： alter table 表名 add 字段名 类型 comment 字段注释
 修改字段类型 ： alter table 表名 modify 字段名 新数据类型 ：
 修改字段与数据类型 ： alter table 表名 change 旧字段名 新字段名 类型 comment 注释 约束：
 删除子段 ： alter table 表名 drop cloumn 字段名 ；
 修改表名字 ； rename table 表名 to 新表名。
 删除表结构： drop table if exists 表名 ;
 
 
```

------

> [!CAUTION]
>
> 设置的数据类型默认为有符号数 ， 变成无符号数在数据类型后面加一个unsigned。
>
> 小数类型  folat（5，2）；

## DML：



```mysql
-- inster语句:
添加数据： insert  into 表名 （字段名 .... ) values (值1 ， );
全部字段添加数据 insert into 表名 values(值1 ...) ,(批量加入， 和前一个相同) ;
-- update语句：
修改数据：update 表名 set 字段名 = 值1 ，where 条件 ；
-- delete语句：
删除数据：delete from 表名 where 条件 ;
```

> [!NOTE]
>
> 1.插入数据时 ， 指定的字段顺序与值的顺序要以一对应
>
> 2.字符串和日期类型应该包含在引号中
>
> 3.插入的数据大小， 应该字段的范围以内
>
> 4.修改语句的条件可以有 ， 也可以没有 ， 如果没有条件 ， 则会修改整张表的所有数据。
>
> 5.删除语句的条件可有可没有
>
> 6. delete语句不能删除某一个字段的值

## DQL：数据查询

```mysql
select 字段列表 from 表明列表  where 条件列表 group by 分组字段列表 having 分组后的列表 order by 排序字段列表 limit 分页参数
-- 条件查询：
查询返回多个字段：select 字段1 字段2 from 表名 ；
查询所有字段 ： slect *from 表名 ；
设置别名 select 字段[as 别名] ， 字段[as 别名] from 表名 ；
去除重复记录 select distinct 字段列表 from 表名 ；
-- 比较运算符
between..and .. 在某个范围内
in（）在in之后的列表中的值 ， 多选一 。
like 占位符 模糊匹配（_匹配单个子符 ， %匹配任意个字符)
is null 是null ；
-- 分组查询：group by ;
   -- 聚合函数：语法：select 聚合函数（字段列表）from 表名 ；
      聚合函数             功能
      count                统计数量
      max                  最大值
      min                  最小值
      avg                  平均值
      sum                  求和
      
group by 分组字段 having 分组后的筛选 ；
-- 排序查询 
  select 字段列表 from 表名 where 条件列表 group by 分组字段 order by 字段1 排列方式  ,字段二 排列方式 
  ASC 升序 ， DESC 降序 ；
 -- 分页查询：
 select 字段列表 from 表名 limit 起始索引 ， 查询记录数 。
 起始索引= （页码-1）*每页查询数。
 -- 案例总结
 if(条件 ， true取值 ， false取值)
 case 表达式 ： when 值1 then 结果1 when 值2 then 结果2 ..... , else ;
  
```

> [!CAUTION]
>
> *号代表查询所有字段 ， 实际中少用 ，影响效率 ，不直观。
>
> 聚合函数对null不统计
>
> 统计总数变量时count（*） ， count（常量） ， count（字段），推荐使用count（*）；
>
> 分组之后 ，查询的字段一般为聚合函数和分组字段， 查询其他字段无意义。
>
> 执行顺序： where > 聚合函数 >having ；
>
> 多字段排序，当第一个字段相同时 ， 才会根据第二个字段进行排序 ；
