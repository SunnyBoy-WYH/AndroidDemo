package com.example.database_manage.teacher;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;

/*
    该界面主要用于修改学生分数的信息
 */
public class change_student_score extends AppCompatActivity {
    private SQLiteDatabase db;
    private Button button_change;
    private Button button_back;
    private Intent intent;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_student_score);

        init();


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_score_change:
                        ContentValues values = new ContentValues();
                        values.put("student_id", textView1.getText().toString());
                        values.put("score", editText.getText().toString());
                        db.update("student_course", values, "student_id = ? AND course_name = ? ",
                                new String[]{textView1.getText().toString(), intent.getStringExtra("course_name")});
                        finish();
                        break;
                    case R.id.button_score_back:
                        finish();
                        break;
                    default:
                }

            }
        };


        button_change.setOnClickListener(listener);
        button_back.setOnClickListener(listener);

    }
    public void init()
    {
        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(change_student_score.this, "test_db");


        //绑定按钮
        button_change = findViewById(R.id.button_score_change);
        button_back = findViewById(R.id.button_score_back);
        //从上一个活动接受数据，并置上去
        intent = getIntent();

        textView1 = findViewById(R.id.t_id);
        textView1.setText(intent.getStringExtra("student_id"));
        textView2 = findViewById(R.id.t_name);
        textView2.setText(intent.getStringExtra("name"));
        textView3 = findViewById(R.id.t_banji);
        textView3.setText(intent.getStringExtra("banji"));
        textView4 = findViewById(R.id.tc_course_name);
        textView4.setText(intent.getStringExtra("course_name"));

        editText = findViewById(R.id.edit_score);
        editText.setText(intent.getStringExtra("score"));
    }
}
