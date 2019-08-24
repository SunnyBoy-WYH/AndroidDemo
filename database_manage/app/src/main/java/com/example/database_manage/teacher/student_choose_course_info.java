package com.example.database_manage.teacher;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class student_choose_course_info extends AppCompatActivity {
    private ListView listView;
    private SQLiteDatabase db;
    private String course_name = "";
    private String teacher_name = "";
    private Button button_banji;
    private Button button_score;
    private Button button_aver;
    private Button button_name;
    private Intent Intent_receive;
    private TextView textView_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_choose_course_info);

        init();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.orderby_score:

                        fenlei_paixu("score");


                        break;
                    case R.id.button_groupby_banji:

                        fenlei_paixu("banji");
                        break;
                    //查看平均成绩
                    case R.id.button_average_score:
                        Cursor cursor3 = db.rawQuery(
                                "select * from student inner join student_course " +
                                        "on student.id =student_course.student_id " +
                                        "where course_name = ? AND teacher_name = ?"
                                , new String[]{course_name, teacher_name});
                        double sum = 0;
                        while (cursor3.moveToNext()) {
                            sum += cursor3.getInt(cursor3.getColumnIndex("score"));
                        }
                        Toast.makeText(student_choose_course_info.this, "平均成绩为" + sum / cursor3.getCount(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.orderby_name:

                        fenlei_paixu("name");

                        break;
                    default:

                        break;
                }
            }
        };
        button_banji.setOnClickListener(listener);
        button_score.setOnClickListener(listener);
        button_aver.setOnClickListener(listener);
        button_name.setOnClickListener(listener);
        //获取课程名称+老师姓名
        course_name = Intent_receive.getStringExtra("course_name");

        teacher_name = Intent_receive.getStringExtra("teacher_name");
        //游标连接查询
        Cursor cursor1 = db.rawQuery(
                "select * from student inner join student_course " +
                        "on student.id =student_course.student_id " +
                        "where course_name = ? AND teacher_name = ?" +
                        "order by student.id ASC", new String[]{course_name, teacher_name});
        fenlei_paixu("student_id");

        //获取这门课有多少人
        int count = cursor1.getCount();


        //为textview赋值
        String tip = "选择" + course_name + "这门课的共有" + (count) + "人";
        textView_tip.setText(tip);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                Intent intent = new Intent(student_choose_course_info.this, change_student_score.class);
                intent.putExtra("student_id", map_item.get("student_id") + "");
                intent.putExtra("name", map_item.get("name") + "");
                intent.putExtra("banji", map_item.get("banji") + "");
                intent.putExtra("course_name", map_item.get("course_name") + "");
                intent.putExtra("score", map_item.get("score") + "");

                startActivity(intent);
            }
        });


    }

    public void init() {
        //声明数据库对象
        db = new CommonDatabase().getSqliteObject(student_choose_course_info.this, "test_db");
        //获取上一个activity的intent对象，以获得它携带的数据
        Intent_receive = getIntent();


        //绑定listview组件
        listView = findViewById(R.id.listview_student_choose_info);

        //绑定textview组件
        textView_tip = findViewById(R.id.text_tongji);

        //button绑定
        button_banji = findViewById(R.id.button_groupby_banji);
        button_score = findViewById(R.id.orderby_score);
        button_aver = findViewById(R.id.button_average_score);
        button_name = findViewById(R.id.orderby_name);

    }

    //分类排序，根据不同的选项排序
    public void fenlei_paixu(String order) {
        Cursor cursor1;
        if (order.equals("name")) {
            db.setLocale(Locale.CHINESE);
            cursor1 = db.rawQuery(
                    "select * from student inner join student_course " +
                            "on student.id =student_course.student_id " +
                            "where course_name = ? AND teacher_name = ?" +
                            "order by name collate localized  asc", new String[]{course_name, teacher_name});
        } else {
            cursor1 = db.rawQuery(
                    "select * from student inner join student_course " +
                            "on student.id =student_course.student_id " +
                            "where course_name = ? AND teacher_name = ?" +
                            "order by " + order + " ASC", new String[]{course_name, teacher_name});
        }


        ArrayList<Map<String, String>> arrayList_student = new ArrayList<Map<String, String>>();
        while (cursor1.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();

            map.put("student_id", cursor1.getString(cursor1.getColumnIndex("student_id")));
            map.put("name", cursor1.getString(cursor1.getColumnIndex("name")));
            map.put("banji", cursor1.getString(cursor1.getColumnIndex("banji")));
            map.put("course_name", cursor1.getString(cursor1.getColumnIndex("course_name")));
            map.put("score", cursor1.getString(cursor1.getColumnIndex("score")));


            arrayList_student.add(map);

        }

        //设置适配器
        SimpleAdapter simpleAdapter = new SimpleAdapter(student_choose_course_info.this, arrayList_student, R.layout.list_item_course_information,
                new String[]{"student_id", "name", "banji", "course_name", "score"}, new int[]{R.id.course_student_id,
                R.id.course_student_name, R.id.course_student_banji, R.id.course_student_coursename, R.id.course_student_score});
        listView.setAdapter(simpleAdapter);


    }

}

