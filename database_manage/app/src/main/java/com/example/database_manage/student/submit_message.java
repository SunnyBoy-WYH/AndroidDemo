package com.example.database_manage.student;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;

public class submit_message extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_message);
        final EditText editText = findViewById(R.id.et_message);
        Button button_submit = findViewById(R.id.button_submit);


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(submit_message.this, "留言不可以为空", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("student_id", getIntent().getStringExtra("student_id"));
                    contentValues.put("message", editText.getText().toString());

                    new CommonDatabase().getSqliteObject(submit_message.this, "test_db").
                            insert("message", null, contentValues);
                    Toast.makeText(submit_message.this, "留言成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });


    }
}
