package com.example.database_manage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
   该界面主要用于建表，以及设定完整性
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    //带全部参数的构造函数，此构造函数必不可少
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //学生信息
        String sql_student = "create table student(" +
                "id int primary key not null ," +
                "name varchar(20) not null," +
                "sex varchar(10) not null," +
                "age int check(age<100 and age>0)," +
                "banji varchar(20) ," +
                "phone int ," +
                "college varchar(20) default '无'," +
                "jidian int check(jidian>0 and jidian<5.0)) ";
        //学生——登陆密码表
        String sql_student_load = "create table load_account(account int primary Key not null ,password varchar(20))";
        //触发器
        String sql_ctrigger = "CREATE TRIGGER load_add AFTER INSERT ON student  " +
                "BEGIN INSERT INTO load_account(account,password) VALUES (new.id,'e10adc3949ba59abbe56e057f20f883eWYH'); END;";
        String sql_trigger = "CREATE TRIGGER delete_load BEFORE DELETE ON student BEGIN DELETE  FROM load_account WHERE account =old.id ;END ; ";
        String sql_trigger2 = "CREATE TRIGGER student_load_change " +
                " AFTER UPDATE ON student BEGIN UPDATE  load_account SET ACCOUNT = NEW.ID WHERE ACCOUNT = OLD.ID; END;";
        db.execSQL(sql_student_load);
        db.execSQL(sql_student);
        db.execSQL(sql_ctrigger);
        db.execSQL(sql_trigger);
        db.execSQL(sql_trigger2);


        //老师个人信息表
        String sql_teacher = "create table teacher(" +
                "teacher_id int primary key not null," +
                "name varchar(20)," +
                "sex  varchar(20) check(sex = '男' or sex = '女')," +

                "age int check(age>0 and age<100)," +
                "level varchar(20)," +
                "phone int ," +
                "college varchar(20))";
        //老师登录信息表
        String sql_teacher_load = "create table load_teacher(" +
                "account int primary key not null ," +
                "password varchar(20))";
        //触发器
        String sql_ttrigger = "CREATE TRIGGER load_add_teacher AFTER INSERT ON teacher  " +
                "BEGIN INSERT INTO load_teacher(account,password) VALUES (new.teacher_id,'e10adc3949ba59abbe56e057f20f883eWYH'); END;";
        String sql_dttrigger = "CREATE TRIGGER delete_teacher_load " +
                " BEFORE DELETE ON teacher BEGIN DELETE FROM load_teacher WHERE ACCOUNT = OLD.TEACHER_ID; END;";
        String sql_dttrigger1 = "CREATE TRIGGER teacher_load_change " +
                " AFTER UPDATE ON teacher BEGIN UPDATE  load_teacher SET ACCOUNT = NEW.TEACHER_ID WHERE ACCOUNT = OLD.TEACHER_ID; END;";

        db.execSQL(sql_teacher_load);
        db.execSQL(sql_teacher);
        db.execSQL(sql_ttrigger);
        db.execSQL(sql_dttrigger);
        db.execSQL(sql_dttrigger1);


        //学生选课信息表
        String sql_student_course = "create table student_course(" +
                "student_id int ," +
                "course_name varchar(20)," +
                "teacher_name varchar(10)," +
                "score int, " +
                "FOREIGN KEY(course_name) REFERENCES course(course_name)," +
                "FOREIGN KEY(teacher_name) REFERENCES course(teacher_name)" +

                ")";
        //课程信息表
        String sql_course = "create table course(" +
                "teacher_name varchar(20) not null," +
                "course_name varchar(20) not null," +
                "course_weight SMALLINT ," +
                "course_time varchar(20)," +
                "course_period varchar(20)," +
                "primary key(teacher_name,course_name)," +
                "FOREIGN KEY(teacher_name) REFERENCES teacher(name))";
        db.execSQL(sql_course);
        db.execSQL(sql_student_course);


        //留言信息存储表
        String sql_liuyan = "create table message(" +
                "student_id int primary key not null," +
                "message text)";
        db.execSQL(sql_liuyan);


        //管理员表
        String sql_admin = "create table administractor(" +
                " account varchar(20)," +
                " password varchar(20))";
        String sql_insert_admin = "insert into administractor values('WYH','c7533ad5fb828c1bedd12608f86f998dWYH')";

        db.execSQL(sql_admin);
        db.execSQL(sql_insert_admin);


        //个人资源配置表，比如更改图片之类的啦
        String personal_resource = "create table personal_resource(" +
                "id int primary key not null ," +
                "IMAGE blob)";

        db.execSQL(personal_resource);

        //触发器

        String personal_resource_trigger = "CREATE TRIGGER personal_resource_trigger AFTER INSERT ON student " +
                "BEGIN INSERT INTO personal_resource(id,IMAGE) VALUES (new.id,null);END;";
        String personal_resource_trigger1 = "CREATE TRIGGER personal_resource_trigger_t AFTER INSERT ON teacher " +
                "BEGIN INSERT INTO personal_resource(id,IMAGE) VALUES (new.teacher_id,null);END;";

        db.execSQL(personal_resource_trigger);
        db.execSQL(personal_resource_trigger1);


        //插入基本测试数据
        String sql_insert1 = "insert into course(teacher_name,course_name,course_weight,course_time,course_period) values('李xx','电子线路基础',2,'周一上午','1')";
        String sql_insert2 = "insert into course(teacher_name,course_name,course_weight,course_time,course_period) values('高x* 刘x','大学英语4',4,'周五上午','1')";
        String sql_insert3 = "insert into course(teacher_name,course_name,course_weight,course_time,course_period) values('魏xx','数据库原理',3,'周三上午','1')";
        String sql_insert4 = "insert into course(teacher_name,course_name,course_weight,course_time,course_period) values('司xx','数学模型',2,'周一晚上','1')";
        String sql_insert5 = "insert into course(teacher_name,course_name,course_weight,course_time,course_period) values('司xx','运筹学',2,'周三下午','1')";
        String sql_insert6 = "insert into course(teacher_name,course_name,course_weight,course_time,course_period) values('王xx','毛泽东思想概论',4.5,'周五晚上','1')";
        db.execSQL(sql_insert1);
        db.execSQL(sql_insert2);
        db.execSQL(sql_insert3);
        db.execSQL(sql_insert4);
        db.execSQL(sql_insert5);
        db.execSQL(sql_insert6);


        //插入基本测试数据
        String sql_p = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(20171302028,'武xx','男',19,'17计科3班',188330,'网络空间安全与计算机学院')";

        String sql_p1 = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(20180301178,'梁x','女',20,'18新闻2班',188330,'新闻传播学院')";
        String sql_p2 = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(20171302029,'台x','男',20,'17计科3班',188330,'网络空间安全与计算机学院')";
        String sql_p3 = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(20171302030,'王x','男',20,'17计科3班',188330,'网络空间安全与计算机学院')";
        String sql_p4 = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(20171302031,'霍xx','男',20,'17材料2班',188330,'化学与环境科学学院')";
        String sql_p5 = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(20171102027,'王xx','男',20,'17材料2班',18812141604,'化学与环境科学学院')";


        String sql_tp1 = "insert into teacher(teacher_id ,name ,sex,age ,level , phone, college )" +
                " values(2018,'魏xx','男',20,'高级教师',188330,'网络空间安全与计算机学院')";
        String sql_tp = "insert into teacher(teacher_id ,name ,sex,age ,level , phone, college )" +
                " values(2017,'司xx','男',19,'高级教师',188330,'网络空间安全与计算机学院')";
        db.execSQL(sql_p);
        db.execSQL(sql_tp);
        db.execSQL(sql_p1);
        db.execSQL(sql_p2);
        db.execSQL(sql_p3);
        db.execSQL(sql_p4);
        db.execSQL(sql_p5);
        db.execSQL(sql_tp1);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
