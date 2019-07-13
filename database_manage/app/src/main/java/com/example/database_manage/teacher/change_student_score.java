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

public class change_student_score extends AppCompatActivity {
    SQLiteDatabase db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_student_score);
        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(change_student_score.this,"test_db");


        //绑定按钮
        Button button_change = findViewById(R.id.button_score_change);
        Button button_back = findViewById(R.id.button_score_back);
        //从上一个活动接受数据，并置上去
        final Intent intent =getIntent();

        final TextView textView1 = findViewById(R.id.t_id);
        textView1.setText(intent.getStringExtra("student_id"));
        final TextView textView2 = findViewById(R.id.t_name);
        textView2.setText(intent.getStringExtra("name"));
        final TextView textView3 = findViewById(R.id.t_banji);
        textView3.setText(intent.getStringExtra("banji"));
        final TextView textView4 = findViewById(R.id.tc_course_name);
        textView4.setText(intent.getStringExtra("course_name"));

        final EditText editText = findViewById(R.id.edit_score);
        editText.setText(intent.getStringExtra("score"));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.button_score_change:
                        ContentValues values = new ContentValues();
                        values.put("student_id",textView1.getText().toString());
                        values.put("score",editText.getText().toString());
                        db.update("student_course", values, "student_id = ? AND course_name = ? ",
                                new String[]{textView1.getText().toString(),intent.getStringExtra("course_name")});
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
}
