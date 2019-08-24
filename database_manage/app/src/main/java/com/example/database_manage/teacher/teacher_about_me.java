package com.example.database_manage.teacher;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;

/*
    用于展示教师信息
 */
public class teacher_about_me extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase db;
    private TextView t_aboutme_id;
    private TextView t_aboutme_name;
    private TextView t_aboutme_sex;
    private TextView t_aboutme_age;
    private TextView t_aboutme_zhicheng;
    private TextView t_aboutme_phone;
    private TextView t_aboutme_college;
    private Button button_finish_about_t;

    //用于传递信息
    private Intent receive_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //绑定xml配置文件
        setContentView(R.layout.activity_teacher_about_me);

        //初始化组件
        initView();

        //去查找目前登录老师的信息
        Cursor cursor_about = db.query("teacher", null, "teacher_id = ?", new String[]{receive_intent.getStringExtra("teacher_id")}, null, null, null);
        while (cursor_about.moveToNext()) {
            t_aboutme_id.setText(cursor_about.getString(cursor_about.getColumnIndex("teacher_id")));
            t_aboutme_name.setText(cursor_about.getString(cursor_about.getColumnIndex("name")));
            t_aboutme_zhicheng.setText(cursor_about.getString(cursor_about.getColumnIndex("level")));
            t_aboutme_phone.setText(cursor_about.getString(cursor_about.getColumnIndex("phone")));
            t_aboutme_sex.setText(cursor_about.getString(cursor_about.getColumnIndex("sex")));
            t_aboutme_age.setText(cursor_about.getString(cursor_about.getColumnIndex("age")));
            t_aboutme_college.setText(cursor_about.getString(cursor_about.getColumnIndex("college")));

        }


    }

    //初始化组件的方法
    private void initView() {

        db = new CommonDatabase().getSqliteObject(teacher_about_me.this, "test_db");

        t_aboutme_id = (TextView) findViewById(R.id.t_aboutme_id);
        t_aboutme_name = (TextView) findViewById(R.id.t_aboutme_name);
        t_aboutme_sex = (TextView) findViewById(R.id.t_aboutme_sex);
        t_aboutme_age = (TextView) findViewById(R.id.t_aboutme_age);
        t_aboutme_zhicheng = (TextView) findViewById(R.id.t_aboutme_zhicheng);
        t_aboutme_phone = (TextView) findViewById(R.id.t_aboutme_phone);
        t_aboutme_college = (TextView) findViewById(R.id.t_aboutme_college);


        button_finish_about_t = (Button) findViewById(R.id.button_finish_about_t);

        button_finish_about_t.setOnClickListener(this);

        //获取上个activity穿过来的intent
        receive_intent = getIntent();


    }

    //用于点击返回后销毁当前活动
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_finish_about_t:
                finish();

                break;
        }
    }
}
