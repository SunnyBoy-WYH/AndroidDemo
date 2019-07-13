package com.example.database_manage.administractor;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;


public class change_course_set extends AppCompatActivity implements View.OnClickListener {
    EditText edit_teacher_name;
    EditText edit_course_name;
    EditText edit_course_time;
    EditText edit_course_period;
    String teacher_name="";
    String course_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_course_set);
        //按钮绑定
        Button button_delete = findViewById(R.id.button_delete_course);

        Button button_baocun = findViewById(R.id.button_change_course);
        //监听器
        button_delete.setOnClickListener(this);
        button_baocun.setOnClickListener(this);
        //编辑框绑定
        edit_teacher_name = findViewById(R.id.et_teacher_name);
        edit_course_name = findViewById(R.id.et_course_name);
        edit_course_time = findViewById(R.id.et_course_time);
        edit_course_period = findViewById(R.id.et_course_period);


        Intent intent_receive = getIntent();



        //把intent中携带的信息设置成为编辑框的初始内容，方便修改
        edit_teacher_name.setText(intent_receive.getStringExtra("teacher_name"));
        edit_course_name.setText(intent_receive.getStringExtra("course_name"));
        edit_course_time.setText(intent_receive.getStringExtra("course_time"));
        edit_course_period.setText(intent_receive.getStringExtra("course_period"));




        //获取初始值
        teacher_name = edit_teacher_name.getText().toString();
        course_name = edit_course_name.getText().toString();


    }

    @Override
    public void onClick(View v) {
        CommonDatabase commonDatabase = new CommonDatabase();
        final SQLiteDatabase db = commonDatabase.getSqliteObject(change_course_set.this,"test_db");

        switch (v.getId())
        {
            case R.id.button_delete_course:
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(change_course_set.this);
                //    设置Title的图标
                builder.setIcon(R.drawable.ic_launcher_background);
                //    设置Title的内容
                builder.setTitle("弹出警告框");
                //    设置Content来显示一个信息
                builder.setMessage("确定删除吗？");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        db.delete("course","teacher_name=? AND course_name = ?",new String[]{teacher_name,course_name});
                        finish();
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

                //    显示出该对话框
                builder.show();

                break;
            /****更改*****/
            case R.id.button_change_course:

                //获取这三项数据，并写入contentvalues中
                ContentValues values2 = new ContentValues();
                values2.put("teacher_name", edit_teacher_name.getText().toString());
                values2.put("course_name", edit_course_name.getText().toString());
                values2.put("course_time", edit_course_time.getText().toString());
                values2.put("course_period", edit_course_period.getText().toString());


                db.update("course", values2, "teacher_name = ? AND course_name = ?", new String[]{teacher_name,course_name});
                finish();
                break;

            default:
                break;




        }
    }


}

