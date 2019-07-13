package com.example.database_manage.administractor;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;


public class delete_change_teacher extends AppCompatActivity implements View.OnClickListener {


        EditText edit_teacher_id ;
        EditText edit_xingming_d;
        EditText edit_level_d;
        EditText edit_sex_d;
        EditText edit_age_d;
        EditText edit_phone_d;
        EditText edit_college_d;
        String  t_d_id ;
        String  t_d_xingming ;
        String  t_d_level ;


        //false代表没点删除，点的是
        boolean flag = false;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_delete_change_teacher);

            ImageButton img1 = findViewById(R.id.call_to_teacher);
            img1.setOnClickListener(this);

            //按钮绑定
            Button button_delete = findViewById(R.id.button_teacher_delete);

            Button button_baocun = findViewById(R.id.button_teacher_baocun);
            //监听器
            button_delete.setOnClickListener(this);
            button_baocun.setOnClickListener(this);


            //编辑框绑定
            edit_teacher_id= findViewById(R.id.edit_teacher_id_d);
            edit_xingming_d = findViewById(R.id.edit_teacher_name_d);
            edit_sex_d = findViewById(R.id.edit_teacher_sex_d);
            edit_age_d = findViewById(R.id.edit_teacher_age_d);
            edit_level_d = findViewById(R.id.edit_level_d);
            edit_phone_d = findViewById(R.id.edit_teacher_phone_d);
            edit_college_d = findViewById(R.id.edit_teacher_college_d);


            Intent intent_receive = getIntent();




            //把intent中携带的信息设置成为编辑框的初始内容，方便修改
            edit_teacher_id.setText(intent_receive.getStringExtra("teacher_id"));
            edit_xingming_d.setText(intent_receive.getStringExtra("name"));
            edit_level_d.setText(intent_receive.getStringExtra("level"));
            edit_sex_d.setText(intent_receive.getStringExtra("sex"));
            edit_age_d.setText(intent_receive.getStringExtra("age"));
            edit_phone_d.setText(intent_receive.getStringExtra("phone"));
            edit_college_d.setText(intent_receive.getStringExtra("college"));



            //获取初始值
            t_d_id = edit_teacher_id.getText().toString();
            t_d_xingming = edit_xingming_d.getText().toString();
            t_d_level = edit_level_d.getText().toString();

        }

        @Override
        public void onClick(View v) {
            CommonDatabase commonDatabase = new CommonDatabase();
            final SQLiteDatabase db = commonDatabase.getSqliteObject(delete_change_teacher.this,"test_db");

            switch (v.getId())
            {
                case R.id.button_teacher_delete:
                    //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                    AlertDialog.Builder builder = new AlertDialog.Builder(delete_change_teacher.this);
                    //    设置Title的图标
                    builder.setIcon(R.drawable.ic_launcher_background);
                    //    设置Title的内容
                    builder.setTitle("警告框");
                    //    设置Content来显示一个信息
                    builder.setMessage("确定删除吗？");
                    //    设置一个PositiveButton
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            db.delete("teacher","teacher_id=?",new String[]{t_d_id});
                            Toast.makeText(delete_change_teacher.this,"删除成功",Toast.LENGTH_SHORT).show();
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
                case R.id.button_teacher_baocun:
                    Cursor cursor = db.query("teacher",null,"teacher_id = ?",
                            new String[]{edit_teacher_id.getText().toString()},null,null,null);
                    if(cursor.getCount()==0)
                    {
                        //获取这三项数据，并写入contentvalues中
                        ContentValues values2 = new ContentValues();

                        values2.put("name", edit_xingming_d.getText().toString());
                        values2.put("teacher_id", edit_teacher_id.getText().toString());
                        values2.put("level", edit_level_d.getText().toString());
                        values2.put("sex", edit_sex_d.getText().toString());
                        values2.put("age", edit_age_d.getText().toString());
                        values2.put("phone", edit_phone_d.getText().toString());
                        values2.put("college", edit_college_d.getText().toString());

                        db.update("teacher", values2, "teacher_id = ? ", new String[]{t_d_id});
                        finish();
                    }
                    else
                    {
                        Toast.makeText(delete_change_teacher.this,"这个教师号已存在！",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.call_to_teacher:
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+edit_phone_d.getText().toString()));
                    startActivity(intent);

                    break;

                default:
                    break;




            }
        }


    }


