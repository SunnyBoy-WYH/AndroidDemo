package com.example.database_manage.administractor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;


public class add_teacher extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        db = new CommonDatabase().getSqliteObject(add_teacher.this,"test_db");

        final EditText et_teacher_id = findViewById(R.id.edit_teacher_id);
        final EditText et_teacher_name = findViewById(R.id.edit_teacher_xingming);
        final EditText et_teacher_sex = findViewById(R.id.edit_teacher_sex);
        final EditText et_teacher_phone = findViewById(R.id.edit_teacher_phone);
        final EditText et_teacher_level = findViewById(R.id.edit_teacher_level);
        final EditText et_teacher_college = findViewById(R.id.edit_teacher_college);


        Button button_add = findViewById(R.id.button_add_teacher_ok);
        Button button_back = findViewById(R.id.button_finish_add_teacher);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.button_add_teacher_ok:
                        ContentValues values = new ContentValues();
                        values.put("teacher_id",et_teacher_id.getText().toString());
                        values.put("name",et_teacher_name.getText().toString());
                        values.put("sex ",et_teacher_sex.getText().toString());
                        values.put("phone",et_teacher_phone.getText().toString());
                        values.put("level",et_teacher_level.getText().toString());
                        values.put("college",et_teacher_college.getText().toString());
                        db.insert("teacher",null,values);
                        finish();


                        break;
                    case R.id.button_finish_add_teacher:
                        finish();
                        break;
                }
            }
        };


        button_add.setOnClickListener(listener);
        button_back.setOnClickListener(listener);
    }
}
