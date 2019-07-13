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


/***
 * 这里主要是点击listview中的学生信息时，对应的相应activity
 */
public class delete_change extends AppCompatActivity implements View.OnClickListener {
    EditText edit_xuehao_d;
    EditText edit_xingming_d;
    EditText edit_banji_d;
    EditText edit_sex_d;
    EditText edit_age_d;
    EditText edit_phone_d;
    EditText edit_college_d;
    String  t_d_id ;
    String  t_d_xingming ;
    String  t_d_banji ;
    //false代表没点删除，点的是
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_change);

        //按钮绑定
        Button button_delete = findViewById(R.id.button_delete);

        Button button_baocun = findViewById(R.id.button_baocun);
        //监听器
        button_delete.setOnClickListener(this);
        button_baocun.setOnClickListener(this);
        //编辑框绑定
        edit_xuehao_d = findViewById(R.id.edit_xuehao_d);
        edit_xingming_d = findViewById(R.id.edit_xingming_d);
        edit_sex_d = findViewById(R.id.edit_sex_d);
        edit_age_d = findViewById(R.id.edit_age_d);
        edit_phone_d = findViewById(R.id.edit_phone_d);
        edit_college_d = findViewById(R.id.edit_college_d);
        edit_banji_d = findViewById(R.id.edit_banji_d);

        Intent intent_receive = getIntent();



        //把intent中携带的信息设置成为编辑框的初始内容，方便修改
        edit_xuehao_d.setText(intent_receive.getStringExtra("id"));
        edit_xingming_d.setText(intent_receive.getStringExtra("name"));
        edit_banji_d.setText(intent_receive.getStringExtra("banji"));
        edit_sex_d.setText(intent_receive.getStringExtra("sex"));
        edit_age_d.setText(intent_receive.getStringExtra("age"));
        edit_phone_d.setText(intent_receive.getStringExtra("phone"));
        edit_college_d.setText(intent_receive.getStringExtra("college"));



        //获取初始值
        t_d_id = edit_xuehao_d.getText().toString();
        t_d_xingming = edit_xingming_d.getText().toString();
        t_d_banji = edit_banji_d.getText().toString();

    }

    @Override
    public void onClick(View v) {
        CommonDatabase commonDatabase = new CommonDatabase();
        final SQLiteDatabase db = commonDatabase.getSqliteObject(delete_change.this,"test_db");

        switch (v.getId())
        {
            case R.id.button_delete:
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(delete_change.this);
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
                        db.delete("student","id=?",new String[]{t_d_id});
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
            case R.id.button_baocun:

                //获取这三项数据，并写入contentvalues中
                ContentValues values2 = new ContentValues();
                values2.put("name", edit_xingming_d.getText().toString());
                values2.put("id", edit_xuehao_d.getText().toString());
                values2.put("banji", edit_banji_d.getText().toString());
                values2.put("sex", edit_sex_d.getText().toString());
                values2.put("age", edit_age_d.getText().toString());
                values2.put("phone", edit_phone_d.getText().toString());
                values2.put("college", edit_college_d.getText().toString());

                db.update("student", values2, "id = ? ", new String[]{t_d_id});
                finish();
                break;

            default:
                break;




        }
    }


}
