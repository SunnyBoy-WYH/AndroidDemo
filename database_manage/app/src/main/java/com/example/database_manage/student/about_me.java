package com.example.database_manage.student;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;

/*
    我的信息功能实现，主要根据登录时传过来的intent所携带的数据
 */
public class about_me extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(about_me.this, "test_db");

        //获取登录时传来的信息
        final Intent intent_about_me = getIntent();

        //绑定组件
        TextView t_about_id = findViewById(R.id.aboutme_id);
        TextView t_about_name = findViewById(R.id.aboutme_name);
        TextView t_about_phone = findViewById(R.id.aboutme_phone);
        TextView t_about_banji = findViewById(R.id.aboutme_banji);
        TextView t_about_sex = findViewById(R.id.aboutme_sex);
        TextView t_about_age = findViewById(R.id.aboutme_age);
        TextView t_about_college = findViewById(R.id.aboutme_college);

        Cursor cursor_about = db.query("student", null, "id = ?", new String[]{intent_about_me.getStringExtra("student_id")}, null, null, null);
        while (cursor_about.moveToNext()) {
            //将通过id查询到的学生信息显示到界面中
            t_about_id.setText(cursor_about.getString(cursor_about.getColumnIndex("id")));
            t_about_name.setText(cursor_about.getString(cursor_about.getColumnIndex("name")));
            t_about_banji.setText(cursor_about.getString(cursor_about.getColumnIndex("banji")));
            t_about_phone.setText(cursor_about.getString(cursor_about.getColumnIndex("phone")));
            t_about_sex.setText(cursor_about.getString(cursor_about.getColumnIndex("sex")));
            t_about_age.setText(cursor_about.getString(cursor_about.getColumnIndex("age")));
            t_about_college.setText(cursor_about.getString(cursor_about.getColumnIndex("college")));

        }
        Button button_back = findViewById(R.id.button_finish_about);
        Button button_jidian = findViewById(R.id.button_query_jidian);
        button_jidian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //主要用于查询绩点
                Cursor cursor1 = db.rawQuery(
                        "select * from student_course inner join course " +
                                "on student_course.course_name =course.course_name " +
                                "AND student_course.teacher_name = course.teacher_name  " +
                                "where student_id = ?", new String[]{intent_about_me.getStringExtra("student_id")});

                //设定比对数组，根据河北大学绩点对照
                double[] duizhao = new double[105];
                double fenmu = 0.0;
                double fenzi = 0.0;
                double temp = 0.8;

                for (int i = 0; i < 60; i++) {
                    duizhao[i] = 0;
                }

                for (int j = 60; j < 66; j++) {
                    temp += 0.2;
                    duizhao[j] = temp;


                }
                temp = 2.0;
                for (int z = 66; z < 95; z++) {
                    temp += 0.1;
                    duizhao[z] = temp;
                }
                for (int k = 95; k <= 100; k++) {
                    duizhao[k] = 5.0;
                }

                while (cursor1.moveToNext()) {
                    if (cursor1.getString(cursor1.getColumnIndex("score")) != null) {
                        fenmu = fenmu + Double.parseDouble(cursor1.getString(cursor1.getColumnIndex("course_weight")));
                        fenzi += duizhao[Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("score")))] * Double.parseDouble(cursor1.getString(cursor1.getColumnIndex("course_weight")));
                    }

                }
                if (fenmu < 0.1) {
                    Toast.makeText(about_me.this, "您还没有选任何课或您的成绩老师还没有注入哦", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(about_me.this, "您的绩点为：" + String.valueOf(fenzi / fenmu), Toast.LENGTH_LONG).show();
                }


            }
        });


        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
