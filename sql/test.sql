create table student(
    sid number primary key,
    sname varchar2(20),
    sage number,
    sex number,
    address varchar2(100)
)

create sequence seq_student_sid;

drop sequence seq_student_sid;

--mysql:
create table student(
    sid int primary key auto_increment,
    sname varchar(50),
    sage int,
    sex int,
    address varchar(100)
)
--存储过程
--根据性别查询学生
delimiter $ -- mysql声明语句的分隔符
create procedure find_student_sex(in v_sex number ,out v_stu_count number)
as
begin
	select count(*) from student where sex=v_sex into v_stu_count;
end;
$

--测试调用存储过程
set @stu_count;
call find_student_sex(1,@stu_count);
select @stu_count;


show tables;

select * from student;