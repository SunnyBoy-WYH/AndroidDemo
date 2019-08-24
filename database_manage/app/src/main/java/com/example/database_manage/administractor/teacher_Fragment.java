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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.database_manage.utils.Common_toolbarColor;
import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class teacher_Fragment extends Fragment {
    SQLiteDatabase db;
    EditText edit_querybyid;
    private Toolbar toolbar;
    private ListView listView;

    String listview_state ="";


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.teacher_toolbar_menu,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.teacher_backup_qingchu:
                listView.setAdapter(null);

                break;
            case R.id.teacher_backup_add:
                Intent intent_add_teacher = new Intent(getActivity(), add_teacher.class);
                startActivity(intent_add_teacher);

                break;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_fragment,null);
        return view;

    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toolbar设置
        toolbar = view.findViewById(R.id.toolbar_fragment_teacher);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //
        setHasOptionsMenu(true);

        //状态栏颜色设置
        new Common_toolbarColor().toolbarColorSet(getActivity());




        //查询教师登陆信息的button
        Button button_look_account = view.findViewById(R.id.f_t_look_teacher);
        //查询教师信息的button
        Button button_query_teacher = view.findViewById(R.id.f_t_query_teacher);
        //按照条件查询老师信息
        Button button_query_teacher_byitem = view.findViewById(R.id.button_query_teacherbyitem);



        final EditText et_jiaoshihao = view.findViewById(R.id.f_edit_query_teacher_byid);
        final EditText et_name= view.findViewById(R.id.f_edit_query_teacher_byname);








        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /******列表视图设置********/
                listView =view.findViewById(R.id.f_t_listview);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        //若是老师状态
                         if(listview_state.equals("teacher"))
                        {
                            HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                            Intent intent_delete = new Intent(getActivity(), delete_change_teacher.class);
                            //获取map中的三项数据，并放入intent
                            intent_delete.putExtra("teacher_id",map_item.get("teacher_id")+"");
                            intent_delete.putExtra("name",map_item.get("name")+"");
                            intent_delete.putExtra("sex",map_item.get("sex")+"");
                            intent_delete.putExtra("age",map_item.get("age")+"");
                            intent_delete.putExtra("level",map_item.get("level")+"");
                            intent_delete.putExtra("phone",map_item.get("phone")+"");
                            intent_delete.putExtra("college",map_item.get("college")+"");
                            startActivity(intent_delete);

                        }
                         else if(listview_state.equals("account_teacher"))
                         {
                             HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                             Intent intent = new Intent(getActivity(), change_account_teacher.class);
                             intent.putExtra("account",map_item.get("account")+"");
                             intent.putExtra("password",map_item.get("password")+"");
                             startActivity(intent);
                         }




                        //

                    }
                });
                /*****建立数据库，并建表*******/
                CommonDatabase commonDatabase= new CommonDatabase();
                db=commonDatabase.getSqliteObject(getActivity(),"test_db");
                /*****设置按钮点击监听器******/
                switch(v.getId()){


                    /*****查询老师信息****/
                    case R.id.f_t_query_teacher:
                        //设置listview状态为老师
                        listview_state="teacher";

                        Cursor cursor_teacher = db.query("teacher", null, null, null, null, null, null);
                        ArrayList<Map<String,String>> arrayList_teacher= new ArrayList<Map<String,String>>();
                        //对游标进行遍历
                        while(cursor_teacher.moveToNext())
                        {

                            Map <String,String> map= new HashMap<String,String>();

                            map.put("teacher_id",cursor_teacher.getString(cursor_teacher.getColumnIndex("teacher_id")));
                            map.put("name",cursor_teacher.getString(cursor_teacher.getColumnIndex("name")));
                            map.put("sex",cursor_teacher.getString(cursor_teacher.getColumnIndex("sex")));
                            map.put("age",cursor_teacher.getString(cursor_teacher.getColumnIndex("age")));
                            map.put("level",cursor_teacher.getString(cursor_teacher.getColumnIndex("level")));
                            map.put("phone",cursor_teacher.getString(cursor_teacher.getColumnIndex("phone")));
                            map.put("college",cursor_teacher.getString(cursor_teacher.getColumnIndex("college")));

                            arrayList_teacher.add(map);

                        }
                        //设置适配器
                        SimpleAdapter simpleAdapter_teacher=new SimpleAdapter(getActivity(),arrayList_teacher,R.layout.list__item_teacher,
                                new String[]{"teacher_id","name","sex","age","level","phone","college"},
                                new int[]{R.id.list_t_id,R.id.list_t_name,R.id.list_t_sex,R.id.list_t_age,
                                        R.id.list_t_level,R.id.list_t_phone,R.id.list_t_college});
                        listView.setAdapter(simpleAdapter_teacher);
                        break;
                    /*****查询教师登陆信息****/
                    case R.id.f_t_look_teacher:
                        //设置listview为老师账户的查询状态
                        listview_state="account_teacher";

                        Cursor cursor_look = db.query("load_teacher", null, null, null, null, null, null);
                        ArrayList<Map<String,String>> arrayList_look= new ArrayList<Map<String,String>>();
                        //对游标进行遍历
                        while(cursor_look.moveToNext())
                        {
                            Map <String,String> map= new HashMap<String,String>();
                            map.put("account",cursor_look.getString(cursor_look.getColumnIndex("account")));
                            map.put("password",cursor_look.getString(cursor_look.getColumnIndex("password")));
                            arrayList_look.add(map);

                        }

                        //设置适配器
                        SimpleAdapter simpleAdapter_look=new SimpleAdapter(getActivity(),arrayList_look,R.layout.list_item_account,
                                new String[]{"account","password"},new int[]{R.id.account_t,R.id.account_tv});
                        listView.setAdapter(simpleAdapter_look);
                        break;

                    case R.id.button_query_teacherbyitem:
                        listview_state = "teacher";

                        final String e_id = et_jiaoshihao.getText().toString();
                        final String e_name = et_name.getText().toString();

                        /****按照教师id查询****/
                        if (!e_id.equals("") && e_name.equals("") ) {

                            Cursor cursor_query_byid = db.query("teacher", null, "teacher_id Like ?", new String[]{"%"+e_id+"%"}, null, null, null);
                            query_byitem(cursor_query_byid,listView);

                        }
                        /****按照姓名查询****/
                        else if (e_id.equals("") && !e_name.equals("") ) {

                            Cursor cursor_query_byid = db.query("teacher", null, " name Like ?", new String[]{"%"+e_name+"%"}, null, null, null);
                            query_byitem(cursor_query_byid,listView);


                        }

                        /****按照id+name查询****/
                        else if(!e_id.equals("") && !e_name.equals("") )
                        {
                            Cursor cursor_query_byid = db.query("teacher", null, "teacher_id Like ? AND name Like ?" , new String[]{"%"+e_id+"%","%"+e_name+"%"}, null, null, null);
                            query_byitem(cursor_query_byid,listView);
                        }


                        else if(e_id.equals("") && e_name.equals("") )
                        {
                            Toast.makeText(getActivity(), "您的查询条件为空!", Toast.LENGTH_SHORT).show();
                        }




                    default:

                        break;

                }

            }


        };


        button_look_account.setOnClickListener(listener);
        button_query_teacher.setOnClickListener(listener);

        button_query_teacher_byitem.setOnClickListener(listener);
    }
    public void query_byitem(Cursor cursor_teacher, ListView listView) {
        ArrayList<Map<String, String>> arrayList_query = new ArrayList<Map<String, String>>();
        if (cursor_teacher.getCount() == 0) {
            listView.setAdapter(null);
            Toast.makeText(getActivity(), "没有查到任何结果", Toast.LENGTH_SHORT).show();
        }
        //对游标进行遍历
        else {
            while (cursor_teacher.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();

                map.put("teacher_id",cursor_teacher.getString(cursor_teacher.getColumnIndex("teacher_id")));
                map.put("name",cursor_teacher.getString(cursor_teacher.getColumnIndex("name")));
                map.put("sex",cursor_teacher.getString(cursor_teacher.getColumnIndex("sex")));
                map.put("age",cursor_teacher.getString(cursor_teacher.getColumnIndex("age")));
                map.put("level",cursor_teacher.getString(cursor_teacher.getColumnIndex("level")));
                map.put("phone",cursor_teacher.getString(cursor_teacher.getColumnIndex("phone")));
                map.put("college",cursor_teacher.getString(cursor_teacher.getColumnIndex("college")));
                arrayList_query.add(map);

            }
            //设置适配器
            SimpleAdapter simpleAdapter_teacher=new SimpleAdapter(getActivity(),arrayList_query,R.layout.list__item_teacher,
                    new String[]{"teacher_id","name","sex","age","level","phone","college"},
                    new int[]{R.id.list_t_id,R.id.list_t_name,R.id.list_t_sex,R.id.list_t_age,
                            R.id.list_t_level,R.id.list_t_phone,R.id.list_t_college});
            listView.setAdapter(simpleAdapter_teacher);
        }

    }
}
