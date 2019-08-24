package com.example.database_manage.administractor;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class student_Fragment extends Fragment {
    private SQLiteDatabase db;
    private EditText edit_querybyid;
    private EditText edit_querybyname;
    private EditText edit_querybybanji;
    //记录listview显示状态，方便设置触发器
    private String listview_state = "";
    private ListView listView;
    private Toolbar toolbar;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.student_toolbar_menu,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.student_backup_qingchu:
                listView.setAdapter(null);

                break;
            case R.id.student_backup_add:
                Intent intent_add1 = new Intent(getActivity(), add.class);
                startActivity(intent_add1);
                break;
            case R.id.student_backup_jidianpaixu:
                jidian_order();


                break;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_fragment, container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Toolbar设置
        toolbar = view.findViewById(R.id.toolbar_fragment_student);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);


        //状态栏颜色设置
        new Common_toolbarColor().toolbarColorSet(getActivity());






        //查询所有学生的按钮
        Button button_query = view.findViewById(R.id.f_query);

        //查询学生登录账号密码的按钮
        Button button_query_account = view.findViewById(R.id.f_query_account);
        //按照学号查询的按钮
        Button button_query_byterm = view.findViewById(R.id.f_query_byterm);


        //按照学号查询的编辑框
        edit_querybyid = view.findViewById(R.id.f_edit_querybyid);
        edit_querybyname = view.findViewById(R.id.f_edit_querybyname);
        edit_querybybanji = view.findViewById(R.id.f_edit_querybybanji);


        /*****建立数据库，并建表*******/
        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(getActivity(), "test_db");
        /***********listview设置******************/
        listView = view.findViewById(R.id.f_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listview_state.equals("student_byterm")||listview_state.equals("student_all")) {
                    //Map <String,Object> map= new HashMap<String,Object>();
                    HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                    Intent intent_delete = new Intent(getActivity(), delete_change.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("id", map_item.get("id") + "");
                    intent_delete.putExtra("name", map_item.get("name") + "");
                    intent_delete.putExtra("sex", map_item.get("sex") + "");
                    intent_delete.putExtra("age", map_item.get("age") + "");
                    intent_delete.putExtra("banji", map_item.get("banji") + "");
                    intent_delete.putExtra("phone", map_item.get("phone") + "");
                    intent_delete.putExtra("college", map_item.get("college") + "");
                    startActivity(intent_delete);
                } else if (listview_state.equals("account")) {
                    HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                    Intent intent = new Intent(getActivity(), change_account.class);
                    intent.putExtra("account", map_item.get("account") + "");
                    intent.putExtra("password", map_item.get("password") + "");
                    startActivity(intent);
                }
                //若是老师状态
                else if (listview_state.equals("teacher")) {
                    HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                    Intent intent_delete = new Intent(getActivity(), delete_change_teacher.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("teacher_id", map_item.get("teacher_id") + "");
                    intent_delete.putExtra("name", map_item.get("name") + "");
                    intent_delete.putExtra("sex", map_item.get("sex") + "");
                    intent_delete.putExtra("age", map_item.get("age") + "");
                    intent_delete.putExtra("level", map_item.get("level") + "");
                    intent_delete.putExtra("phone", map_item.get("phone") + "");
                    intent_delete.putExtra("college", map_item.get("college") + "");
                    startActivity(intent_delete);

                } else if (listview_state.equals("change_course_set")) {
                    HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                    Intent intent_delete = new Intent(getActivity(), change_course_set.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("teacher_name", map_item.get("teacher_name") + "");
                    intent_delete.putExtra("course_name", map_item.get("course_name") + "");
                    intent_delete.putExtra("course_time", map_item.get("course_time") + "");
                    intent_delete.putExtra("course_period", map_item.get("course_period") + "");

                    startActivity(intent_delete);
                }


            }
        });



        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*****设置按钮点击监听器******/
                switch (v.getId()) {

                    case R.id.f_query_byterm:
                        listview_state = "student_byterm";
                        listview_set(listview_state);
                        break;


                    /***************************************查询所有学生信息*********************************/
                    case R.id.f_query:
                        //查询的是学生表,所以
                        listview_state = "student_all";
                        listview_set(listview_state);
                        break;


                    /******查询所有学生用户账号密码*******/
                    case R.id.f_query_account:
                        //查询的是账户，所以设置listview为查询账户状态
                        listview_state = "account";

                        listview_set(listview_state);
                        break;


                    default:

                        break;

                }

            }


        };


        button_query.setOnClickListener(listener);

        //查询学生登录账号密码的按钮
        button_query_account.setOnClickListener(listener);
        //按照学号查询的按钮
        button_query_byterm.setOnClickListener(listener);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.student_swipe_refresh);
        //进度条刷新旋钮的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //设置下拉刷新的监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // refresh ui 的操作代码
                                listview_set(listview_state);

                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });


                    }
                }).start();


            }
        });



    }

    public void query_byitem(Cursor cursor_query_byid, ListView listView) {
        ArrayList<Map<String, String>> arrayList_query = new ArrayList<Map<String, String>>();
        if (cursor_query_byid.getCount() == 0) {
            listView.setAdapter(null);
            Toast.makeText(getActivity(), "没有查到任何结果", Toast.LENGTH_SHORT).show();
        }
        //对游标进行遍历
        else {
            while (cursor_query_byid.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();

                map.put("name", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("name")));
                map.put("id", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("id")));
                map.put("sex", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("sex")));
                map.put("age", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("age")));
                map.put("banji", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("banji")));
                map.put("phone", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("phone")));
                map.put("college", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("college")));
                arrayList_query.add(map);

            }
            //设置适配器
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), arrayList_query, R.layout.list_item,
                    new String[]{"name", "id", "sex", "age", "banji", "phone", "college"}, new int[]{R.id.iname, R.id.iid, R.id.isex, R.id.iage,
                    R.id.ibanji, R.id.iphone, R.id.icollege});
            listView.setAdapter(simpleAdapter);
        }

    }
    public void listview_set(String state)
    {
        if(state.equals("student_all"))
        {
            Cursor cursor = db.query("student", null, null, null, null, null, null);
            ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
            //对游标进行遍历
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();

                map.put("name", cursor.getString(cursor.getColumnIndex("name")));
                map.put("id", cursor.getString(cursor.getColumnIndex("id")));
                map.put("sex", cursor.getString(cursor.getColumnIndex("sex")));
                map.put("age", cursor.getString(cursor.getColumnIndex("age")));
                map.put("banji", cursor.getString(cursor.getColumnIndex("banji")));
                map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
                map.put("college", cursor.getString(cursor.getColumnIndex("college")));

                arrayList.add(map);

            }
            //设置适配器
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), arrayList, R.layout.list_item,
                    new String[]{"name", "id", "sex", "age", "banji", "phone", "college"}, new int[]{R.id.iname, R.id.iid, R.id.isex, R.id.iage,
                    R.id.ibanji, R.id.iphone, R.id.icollege});
            listView.setAdapter(simpleAdapter);
        }
        else if(state.equals("student_byterm"))
        {
            final String e_id = edit_querybyid.getText().toString();
            final String e_name = edit_querybyname.getText().toString();
            final String e_banji = edit_querybybanji.getText().toString();
            /****按照id查询****/
            if (!e_id.equals("") && e_name.equals("") && e_banji.equals("")) {

                Cursor cursor_query_byid = db.query("student", null, "id LIKE?", new String[]{"%"+e_id+"%"}, null, null, null);
                query_byitem(cursor_query_byid,listView);

            }
            /****按照姓名查询****/
            else if (e_id.equals("") && !e_name.equals("") && e_banji.equals("")) {

                //Cursor cursor_query_byid = db.query("student", null, "name = ?", new String[]{e_name}, null, null, null);
                Cursor cursor_query_byid = db.rawQuery("select * from student where name LIKE ?",new String[]{"%"+e_name+"%"});
                query_byitem(cursor_query_byid,listView);


            }
            /****按照班级查询****/
            else if(e_id.equals("") && e_name.equals("") && !e_banji.equals(""))
            {

                Cursor cursor_query_byid = db.query("student", null, "banji LIKE ?", new String[]{"%"+e_banji+"%"}, null, null, null);
                query_byitem(cursor_query_byid,listView);
            }
            /****按照id+name查询****/
            else if(!e_id.equals("") && !e_name.equals("") && e_banji.equals(""))
            {
                Cursor cursor_query_byid = db.query("student", null, "id LIKE ? AND name LIKE ?" , new String[]{"%"+e_id+"%","%"+e_name+"%"}, null, null, null);
                query_byitem(cursor_query_byid,listView);
            }
            /****按照id+banji查询****/
            else if(!e_id.equals("") && e_name.equals("") && !e_banji.equals(""))
            {
                Cursor cursor_query_byid = db.query("student", null, "id LIKE ? AND  banji LIKE ?", new String[]{"%"+e_id+"%","%"+e_banji+"%"}, null, null, null);
                query_byitem(cursor_query_byid,listView);
            }
            /****按照name+banji查询****/
            else if(e_id.equals("") && !e_name.equals("") && !e_banji.equals(""))
            {
                Cursor cursor_query_byid = db.query("student", null, "name LIKE ? AND banji LIKE ?", new String[]{"%"+e_name+"%" ,"%"+e_banji+"%"}, null, null, null);
                query_byitem(cursor_query_byid,listView);
            }
            else if(e_id.equals("") && e_name.equals("") && e_banji.equals(""))
            {
                Toast.makeText(getActivity(), "您的查询条件为空!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(state.equals("account"))
        {
            Cursor cursor_account = db.query("load_account", null, null, null, null, null, null);
            ArrayList<Map<String, String>> arrayList_account = new ArrayList<Map<String, String>>();
            //对游标进行遍历
            while (cursor_account.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("account", cursor_account.getString(cursor_account.getColumnIndex("account")));
                map.put("password", cursor_account.getString(cursor_account.getColumnIndex("password")));
                arrayList_account.add(map);

            }

            //设置适配器
            SimpleAdapter simpleAdapter_account = new SimpleAdapter(getActivity(), arrayList_account, R.layout.list_item_account,
                    new String[]{"account", "password"}, new int[]{R.id.account_t, R.id.account_tv});
            listView.setAdapter(simpleAdapter_account);
        }

    }
    public void jidian_order()
    {
        double[] duizhao = new double[105];
        double fenmu =0.0;
        double fenzi =0.0;
        double temp=0.8;

        for(int i=0;i<60;i++)
        {
            duizhao[i]=0;
        }

        for(int j=60;j<66;j++)
        {
            temp+=0.2;
            duizhao[j]=temp;


        }
        temp=2.0;
        for(int z=66;z<95;z++)
        {
            temp+=0.1;
            duizhao[z]=temp;
        }
        for(int k=95;k<=100;k++)
        {
            duizhao[k]=5.0;
        }
        /*****************************************/
        //去找所有学生的学号
        Cursor cursor_id = db.query("student",null,null,null,null,null,null);
        while(cursor_id.moveToNext())
        {
            //对每个学号进行查询
            String str = cursor_id.getString(cursor_id.getColumnIndex("id"));
            Cursor cursor1 =db.rawQuery("select * " +
                    "from student , student_course,course " +
                    "where student.id = student_course.student_id and course.course_name = student_course.course_name and id = ?",new String[]{str});
            if(cursor1.getCount()==0)
            {
                Toast.makeText(getActivity(),"kong!"+str,Toast.LENGTH_SHORT).show();
                //Toast.makeText(Container.this,"您还没有选任何课或您的成绩老师还没有注入哦",Toast.LENGTH_LONG).show();
            }
            else
            {
                while(cursor1.moveToNext())
                {
                    if(cursor1.getString(cursor1.getColumnIndex("score"))!=null) {
                        fenmu = fenmu + Double.parseDouble(cursor1.getString(cursor1.getColumnIndex("course_weight")));
                        fenzi += duizhao[Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("score")))] * Double.parseDouble(cursor1.getString(cursor1.getColumnIndex("course_weight")));
                    }

                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("jidian",fenzi/fenmu);
                db.update("student", contentValues, "id = ? ", new String[]{str});
                fenmu =0.0;
                fenzi =0.0;
            }



        }
        //然后对列表进行处理
        Cursor cursor_jidian = db.query("student", null, null, null, null, null, "banji");
        ArrayList<Map<String, String>> arrayList_jidian = new ArrayList<Map<String, String>>();
        //对游标进行遍历
        while (cursor_jidian.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", cursor_jidian.getString(cursor_jidian.getColumnIndex("id")));
            map.put("name", cursor_jidian.getString(cursor_jidian.getColumnIndex("name")));
            map.put("banji", cursor_jidian.getString(cursor_jidian.getColumnIndex("banji")));
            map.put("jidian", cursor_jidian.getString(cursor_jidian.getColumnIndex("jidian")));
            arrayList_jidian.add(map);

        }

        //设置适配器
        SimpleAdapter simpleAdapter_jidian = new SimpleAdapter(getActivity(), arrayList_jidian, R.layout.list_item_jidian,
                new String[]{"id", "name","banji","jidian"}, new int[]{R.id.xuehao, R.id.xingming,R.id.banji,R.id.jidian});
        listView.setAdapter(simpleAdapter_jidian);
    }




}
