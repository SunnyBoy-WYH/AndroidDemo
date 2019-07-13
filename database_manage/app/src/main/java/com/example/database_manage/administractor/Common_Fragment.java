package com.example.database_manage.administractor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;



import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Common_Fragment extends Fragment {
    String listview_state="";
    SQLiteDatabase db;
    private Toolbar toolbar;
    private ListView listView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.common_toolbar_menu,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.guanliyuan_backup_add:
                startActivity(new Intent(getActivity(),add_admin.class));


                break;

        }
        return true;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment,null);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toolbar设置
        toolbar = view.findViewById(R.id.toolbar_fragment_common);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        //查看课程信息的button
        Button button_query_course = view.findViewById(R.id.f_look_sumcourse);
        //查看留言的button
        Button button_look_message = view.findViewById(R.id.f_query_liuyan);


        //查看管理员信息
        Button button_look_admin = view.findViewById(R.id.look_admin);

        db = new CommonDatabase().getSqliteObject(getActivity(), "test_db");
        listView = view.findViewById(R.id.f_c_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listview_state.equals("change_course_set"))
                {
                    HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                    Intent intent_delete = new Intent(getActivity(), change_course_set.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("teacher_name",map_item.get("teacher_name")+"");
                    intent_delete.putExtra("course_name",map_item.get("course_name")+"");
                    intent_delete.putExtra("course_time",map_item.get("course_time")+"");
                    intent_delete.putExtra("course_period",map_item.get("course_period")+"");

                    startActivity(intent_delete);
                }
                else if (listview_state.equals("message"))
                {

                }
                else if(listview_state.equals("admin"))
                {
                    HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                    Intent intent_delete = new Intent(getActivity(), change_account_admin.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("account",map_item.get("account")+"");
                    intent_delete.putExtra("password",map_item.get("password")+"");


                    startActivity(intent_delete);
                }

            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    /******查看课程设置情况******///
                    case R.id.f_look_sumcourse:
                        listview_state = "change_course_set";

                        Cursor cursor_look_course = db.query("course", null, null, null, null, null, null);
                        ArrayList<Map<String, String>> arrayList_look_course = new ArrayList<Map<String, String>>();
                        //对游标进行遍历
                        while (cursor_look_course.moveToNext()) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("teacher_name", cursor_look_course.getString(cursor_look_course.getColumnIndex("teacher_name")));
                            map.put("course_name", cursor_look_course.getString(cursor_look_course.getColumnIndex("course_name")));
                            map.put("course_weight", cursor_look_course.getString(cursor_look_course.getColumnIndex("course_weight")));
                            map.put("course_time", cursor_look_course.getString(cursor_look_course.getColumnIndex("course_time")));
                            map.put("course_period", cursor_look_course.getString(cursor_look_course.getColumnIndex("course_period")));

                            arrayList_look_course.add(map);

                        }

                        //设置适配器
                        SimpleAdapter simpleAdapter_look_course = new SimpleAdapter(getActivity(), arrayList_look_course, R.layout.list_item_allcourse,
                                new String[]{"teacher_name", "course_name", "course_weight","course_time", "course_period"}, new int[]{R.id.text_teacher_name, R.id.text_course_name, R.id.text_course_weight,R.id.text_course_time, R.id.text_course_period});
                        listView.setAdapter(simpleAdapter_look_course);

                        break;
                    /****查看留言*******/
                    case R.id.f_query_liuyan:

                        listview_state = "message";
                        Cursor cursor_look_message = db.rawQuery("select * from  message inner join student on student.id = message.student_id ",new String[]{});
                        if (cursor_look_message.getCount() == 0) {
                            Toast.makeText(getActivity(), "还没有任何留言哦~", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<Map<String, String>> arrayList_look_message = new ArrayList<Map<String, String>>();
                            //对游标进行遍历
                            while (cursor_look_message.moveToNext()) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("student_id", cursor_look_message.getString(cursor_look_message.getColumnIndex("student_id")));
                                map.put("name", cursor_look_message.getString(cursor_look_message.getColumnIndex("name")));
                                map.put("banji", cursor_look_message.getString(cursor_look_message.getColumnIndex("banji")));
                                map.put("message", cursor_look_message.getString(cursor_look_message.getColumnIndex("message")));

                                arrayList_look_message.add(map);

                            }

                            //设置适配器
                            SimpleAdapter simpleAdapter_look_message = new SimpleAdapter(getActivity(), arrayList_look_message, R.layout.list_item_message,
                                    new String[]{"student_id","name","banji", "message"}, new int[]{R.id.message_id, R.id.message_name,R.id.message_banji,R.id.message_content});
                            listView.setAdapter(simpleAdapter_look_message);

                        }

                        break;

                    case R.id.look_admin:
                        listview_state = "admin";
                        Cursor cursor = db.query("administractor",null,null,null,null,null, null);
                        ArrayList<Map<String, String>> arrayList_look_admin = new ArrayList<Map<String, String>>();
                        while(cursor.moveToNext())
                        {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("account",cursor.getString(cursor.getColumnIndex("account")));
                            map.put("password",cursor.getString(cursor.getColumnIndex("password")));
                            arrayList_look_admin.add(map);

                        }
                        SimpleAdapter simpleAdapter_look_admin = new SimpleAdapter(getActivity(), arrayList_look_admin, R.layout.list_item_account,
                                new String[]{"account","password"}, new int[]{R.id.account_t, R.id.account_tv});
                        listView.setAdapter(simpleAdapter_look_admin);
                        break;
                    default:
                        break;

                }
            }


        };

        button_query_course.setOnClickListener(listener);
         button_look_message.setOnClickListener(listener);
         button_look_admin.setOnClickListener(listener);
    }

}
