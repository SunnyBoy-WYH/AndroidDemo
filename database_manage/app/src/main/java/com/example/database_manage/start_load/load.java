package com.example.database_manage.start_load;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.administractor.Container;
import com.example.database_manage.database.CommonDatabase;
import com.example.database_manage.student.activity_student;
import com.example.database_manage.teacher.activity_teacher;

import static com.example.database_manage.database.MD5Demo.md5;


/***
 * 登陆界面的配置
 */
public class load extends AppCompatActivity {

    //标记是选择了学生还是管理员
    private String state = "";
    private SQLiteDatabase db;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    private boolean isRemember;
    private EditText edit_load_zhanghao;
    private EditText edit_load_password;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        //用于记住密码的实现
        rememberPass = findViewById(R.id.checkbox_remember);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        isRemember = pref.getBoolean("remember_password", false);


        //状态栏融为一体的方法，这里参考了郭神的博客
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


        //轮子TextputLayout
        final TextInputLayout textInputLayout_p = findViewById(R.id.textinput_p);

        final TextInputLayout textInputLayout_id = findViewById(R.id.input_id);

        //账号密码编辑框
        edit_load_zhanghao = findViewById(R.id.edit_load_zhanghao);

        edit_load_password = findViewById(R.id.edit_load_password);
        //获取数据库对象
        CommonDatabase commonDatabase = new CommonDatabase();

        db = commonDatabase.getSqliteObject(load.this, "test_db");

        //绑定组件
        final Button button_change_password = findViewById(R.id.button_change_password);

        RadioGroup load_radiogroup = findViewById(R.id.load_radiogroup);

        final ImageButton imageButton = findViewById(R.id.imagebutton);

        //眼睛睁开闭上
        edit_load_password.setTransformationMethod(PasswordTransformationMethod.getInstance());//初始为隐藏密文状态

        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) //按下重新设置背景图片

                {

                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    edit_load_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示


                } else if (event.getAction() == MotionEvent.ACTION_UP) //松手恢复原来图片

                {

                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                    edit_load_password.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏

                }

                return false;

            }


        });


        //单选按钮监听器
        load_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radiobutton_xuesheng:

                        textInputLayout_id.setHint("请输入您的学号");
                        button_change_password.setVisibility(View.VISIBLE);

                        state = "student";

                        break;
                    case R.id.radiobutton_guanliyuan:

                        textInputLayout_id.setHint("请输入您的账号");


                        state = "guanliyuan";
                        break;
                    case R.id.radiobutton_teacher:

                        textInputLayout_id.setHint("请输入您的账号");


                        state = "teacher";
                        break;
                    default:
                        break;

                }
            }
        });

        //登录按钮
        Button button_load = findViewById(R.id.button_load);
        button_load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //获取学生输入的账号密码
                String account_load = edit_load_zhanghao.getText().toString();
                String password_load = md5(edit_load_password.getText().toString());
                if (state == "guanliyuan") {
                    String true_password = "";

                    Cursor cursor = db.query("administractor", null, "account = ?", new String[]{account_load}, null, null, null);

                    while (cursor.moveToNext()) {
                        true_password = cursor.getString(cursor.getColumnIndex("password"));
                    }

                    if (password_load.equals(true_password)) {
                        isr();
                        startActivity(new Intent(load.this, Container.class));
                        Toast.makeText(load.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(load.this, "密码错误！", Toast.LENGTH_SHORT).show();
                    }
                } else if (state.equals("student")) {

                    //去数据库中找账号为account的数据

                    String true_password = "";

                    Cursor cursor = db.query("load_account", null, "account = ?", new String[]{account_load}, null, null, null);

                    while (cursor.moveToNext()) {
                        true_password = cursor.getString(cursor.getColumnIndex("password"));
                    }

                    if (password_load.equals(true_password) && true_password.equals("") == false) {
                        isr();
                        Intent intent = new Intent(load.this, activity_student.class);
                        intent.putExtra("student_id", account_load);
                        startActivity(intent);
                        Toast.makeText(load.this, "登陆成功！", Toast.LENGTH_SHORT).show();

                        finish();

                    } else {
                        Toast.makeText(load.this, "密码错误！", Toast.LENGTH_SHORT).show();
                    }


                } else if (state.equals("teacher")) {
                    //去数据库中找账号为account的数据


                    Cursor cursor = db.query("load_teacher", null, "account = ? AND password = ?", new String[]{account_load, password_load}, null, null, null);


                    if (cursor.getCount() == 0) {

                        Toast.makeText(load.this, "密码错误！", Toast.LENGTH_SHORT).show();

                    } else {
                        isr();
                        Intent intent = new Intent(load.this, activity_teacher.class);
                        intent.putExtra("teacher_id", edit_load_zhanghao.getText().toString());
                        startActivity(intent);
                        Toast.makeText(load.this, "登陆成功！", Toast.LENGTH_SHORT).show();

                        finish();
                    }


                } else {
                    Toast.makeText(load.this, "您还没有选择任何登录类型！", Toast.LENGTH_SHORT).show();
                }


            }
        });
        //点击修改按钮进入更改密码的界面

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(load.this, password_change.class);
                startActivity(intent);


            }
        });
        edit_load_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_load_password.getText().toString().equals("")) {
                    textInputLayout_p.setError("不能为空！");
                } else {
                    if (textInputLayout_p.getCounterMaxLength() < edit_load_password.length()) {
                        textInputLayout_p.setError("超出字数限制！");
                    } else {
                        textInputLayout_p.setErrorEnabled(false);
                    }
                }


            }
        });
        //如果手机有记录
        if (isRemember) {
            edit_load_zhanghao.setText(pref.getString("account", ""));
            edit_load_password.setText(pref.getString("password", ""));

            //
            rememberPass.setChecked(true);
        }


    }


    //用于实现记住密码的方法，这里使用SharedPreference存放临时数据
    private void isr() {
        editor = pref.edit();
        //检查复选框是否被选中
        if (rememberPass.isChecked()) {
            editor.putBoolean("remember_password", true);
            editor.putString("account", edit_load_zhanghao.getText().toString());
            editor.putString("password", edit_load_password.getText().toString());
        } else {
            editor.clear();
        }
        editor.apply();
    }

}
