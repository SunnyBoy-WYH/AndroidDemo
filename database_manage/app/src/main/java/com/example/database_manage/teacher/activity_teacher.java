package com.example.database_manage.teacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database_manage.utils.Common_methon;
import com.example.database_manage.utils.Common_toolbarColor;
import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;
import com.example.database_manage.database.image_store;
import com.example.database_manage.start_load.load;
import com.example.database_manage.utils.alertDialog_builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
/*
    教师端主界面
 */

public class activity_teacher extends AppCompatActivity {

    //用于显示信息的列表listview
    private ListView listView;

    //数据库对象，用来操作数据库
    private SQLiteDatabase db;

    //侧滑
    private DrawerLayout drawerLayout;

    //记录状态
    private String state = "";

    //用于下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;

    //用于接收的intent
    private Intent receive_intent;

    //用于替代ActionBar的toolbar
    private Toolbar toolbar;

    //触发查看我的课程信息的button
    private Button button_look_mycourse;

    //圆形imageview 用于显示头像
    private CircleImageView circleImageView;

    //初始自带的actionbar
    private ActionBar actionBar;

    //
    private NavigationView navigationView;

    //对话框类
    private AlertDialog.Builder builder;

    //navigationView的顶部view
    private View headview;

    //登录成功后，侧滑菜单显示的用户身份标识
    private TextView textView_welcome;

    //以下用于点击头像拍照并保存到圆形imageview功能
    private Uri imageUri;
    private static final int TAKE_PHOTO = 1;
    private image_store imageStore;
    private Bitmap bitmap_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher);

        init();

        //去查找该用户有没有头像
        bitmap_temp = imageStore.getBmp(db, receive_intent.getStringExtra("teacher_id"));

        //如果有头像，那就把查到的这个放大头像
        if (bitmap_temp != null) {
            circleImageView.setImageBitmap(bitmap_temp);
        }
        //监听navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_myinfo_t:
                        Intent intent_about = new Intent(activity_teacher.this, teacher_about_me.class);
                        intent_about.putExtra("teacher_id", receive_intent.getStringExtra("teacher_id"));
                        startActivity(intent_about);

                        break;

                    //修改账户
                    case R.id.nav_menu_changeacc_t:

                        builder = new alertDialog_builder(activity_teacher.this).build();
                        //    显示出该对话框

                        builder.show();
                        break;
                    //查看留言
                    case R.id.nav_menu_lookliuyan:
                        Cursor cursor_look = db.query("message", null, null, null, null, null, null);

                        //对游标进行遍历
                        if (cursor_look.getCount() == 0) {
                            Toast.makeText(activity_teacher.this, "还没有任何留言", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<Map<String, String>> arrayList_mycourse = new ArrayList<Map<String, String>>();
                            while (cursor_look.moveToNext()) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("message", cursor_look.getString(cursor_look.getColumnIndex("message")));
                                arrayList_mycourse.add(map);

                            }
                            //设置适配器
                            SimpleAdapter simpleAdapter = new SimpleAdapter(activity_teacher.this, arrayList_mycourse, R.layout.list_item_message_t_look,
                                    new String[]{"message"}, new int[]{R.id.text});
                            listView.setAdapter(simpleAdapter);

                        }

                    default:
                        break;
                }

                return true;
            }
        });

        //为listview设定监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /****点击老师拥有的课程的****/

                //获取用户-老师的名字
                String teacher_name = "";
                Cursor cursor_look_for_id = db.query("teacher", null, "teacher_id =?", new String[]{receive_intent.getStringExtra("teacher_id")}, null, null, null);
                while (cursor_look_for_id.moveToNext()) {
                    teacher_name = cursor_look_for_id.getString(cursor_look_for_id.getColumnIndex("name"));
                }

                HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);
                //获取选择这个子项的课程名称 ，去查找这个课程下的学生
                String course_name = map_item.get("course_name") + "";

                Intent intent_info = new Intent(activity_teacher.this, student_choose_course_info.class);
                intent_info.putExtra("course_name", course_name);
                intent_info.putExtra("teacher_name", teacher_name);
                startActivity(intent_info);


            }
        });

        //为SwipeRefreshLayout设置颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        //为SwipeRefreshLayout设置监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                look_course();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();

            }
        });


        //按钮监听器
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.button_lookmycourse:
                        state = "mycourse";
                        look_course();

                        break;

                    case R.id.circleimage_teacher:
                        // 创建File对象，用于存储拍照后的图片
                        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT < 24) {
                            imageUri = Uri.fromFile(outputImage);
                        } else {
                            imageUri = FileProvider.getUriForFile(activity_teacher.this, "com.example.database_manage.fileprovider", outputImage);
                        }
                        // 启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);

                        break;

                }
            }
        };
        button_look_mycourse.setOnClickListener(listener);
        circleImageView.setOnClickListener(listener);

    }

    //初始化组件
    public void init() {


        //设置标题栏与状态栏颜色保持一致
        new Common_toolbarColor().toolbarColorSet(activity_teacher.this);

        //设置toolbar
        toolbar = findViewById(R.id.toolbar_teacher);
        setSupportActionBar(toolbar);

        //actionbar设置
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.a);
        }


        //DrawerLayout设置
        drawerLayout = findViewById(R.id.drawerlayout_teacher);

        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(activity_teacher.this, "test_db");

        receive_intent = getIntent();

        swipeRefreshLayout = findViewById(R.id.teacher_SwipeRefreshLayout);

        listView = findViewById(R.id.listview_teacher);

        button_look_mycourse = findViewById(R.id.button_lookmycourse);

        //NavigationView绑定及监听子项
        navigationView = findViewById(R.id.navigation_view_t);

        headview = navigationView.inflateHeaderView(R.layout.headlayout_teacher);

        textView_welcome = headview.findViewById(R.id.welcome_textview_teacher);

        circleImageView = headview.findViewById(R.id.circleimage_teacher);

        //表示欢迎的textview
        textView_welcome.setText(receive_intent.getStringExtra("teacher_id"));

        //头像初始化

        imageStore = new image_store();

    }

    //点击头像进行拍照的回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示到头像中
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        circleImageView.setImageBitmap(bitmap);

//更新本人资源表
                        Bitmap bitmap1 = new Common_methon().compressBoundsBitmap(activity_teacher.this, imageUri, 200, 200);
                        imageStore.update(bitmap1, db, receive_intent.getStringExtra("teacher_id"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    }

                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

                break;
            default:
                break;
        }
        return true;
    }

    public void look_course() {
        String teacher_name = "";
        //去老师信息表中根据老师的id查找它的姓名
        Cursor cursor_look_for_id = db.query("teacher", null, "teacher_id =?", new String[]{receive_intent.getStringExtra("teacher_id")}, null, null, null);
        while (cursor_look_for_id.moveToNext()) {
            teacher_name = cursor_look_for_id.getString(cursor_look_for_id.getColumnIndex("name"));
        }

        //以老师姓名为根本去查询老师旗下的课程信息
        Cursor cursor = db.query("course", null, "teacher_name = ?", new String[]{teacher_name}, null, null, null);

        //对游标进行遍历
        if (cursor.getCount() == 0) {
            Toast.makeText(activity_teacher.this, "您没有任何课", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Map<String, String>> arrayList_mycourse = new ArrayList<Map<String, String>>();
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();

                map.put("course_name", cursor.getString(cursor.getColumnIndex("course_name")));
                map.put("course_time", cursor.getString(cursor.getColumnIndex("course_time")));
                map.put("course_period", cursor.getString(cursor.getColumnIndex("course_period")));


                arrayList_mycourse.add(map);

            }
            ////////////////////
            CoordinatorLayout teacher_coordinatorlayout = findViewById(R.id.teacher_coordinatorlayout);
            Snackbar.make(teacher_coordinatorlayout, "您共有" + arrayList_mycourse.size() + "门课程", Snackbar.LENGTH_LONG)
                    .setAction("好的", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();

            //设置适配器
            SimpleAdapter simpleAdapter = new SimpleAdapter(activity_teacher.this, arrayList_mycourse, R.layout.list_item_teacher_mycourse,
                    new String[]{"course_name", "course_time", "course_period"}, new int[]{R.id.t_mycourse_name, R.id.t_mycourse_time, R.id.t_mycourse_period});
            listView.setAdapter(simpleAdapter);


        }
    }
}
