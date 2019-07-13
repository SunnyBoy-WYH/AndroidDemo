package com.example.database_manage.administractor;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.database_manage.R;


public class Container extends AppCompatActivity {
    private student_Fragment student_Fragment;
    private teacher_Fragment teacher_Fragment;
    private Common_Fragment common_fragment;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);




        final ImageButton ib1 = findViewById(R.id.ib1);
        final ImageButton ib2 = findViewById(R.id.ib2);
        final ImageButton ib3 = findViewById(R.id.ib3);

        ib1.setBackgroundColor(R.color.colorfragment);
        ib2.setBackgroundColor(R.color.colorfragment);
        ib3.setBackgroundColor(R.color.colorfragment);

        student_Fragment = new student_Fragment();
        teacher_Fragment = new teacher_Fragment();
        common_fragment  = new Common_Fragment();





        getSupportFragmentManager().beginTransaction().add(R.id.framelayout, student_Fragment).commitAllowingStateLoss();

        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {


//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                Fragment f = null;
                switch (v.getId())
                {
                    case R.id.ib1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, student_Fragment).addToBackStack(null).commitAllowingStateLoss();
                        ib1.setBackgroundColor(Color.WHITE);
                        ib2.setBackgroundColor(R.color.colorfragment);
                        ib3.setBackgroundColor(R.color.colorfragment);





                        break;
                    case R.id.ib2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, teacher_Fragment).addToBackStack(null).commitAllowingStateLoss();
                        ib2.setBackgroundColor(Color.WHITE);
                        ib1.setBackgroundColor(R.color.colorfragment);
                        ib3.setBackgroundColor(R.color.colorfragment);

                        break;
                    case R.id.ib3:

                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, common_fragment).addToBackStack(null).commitAllowingStateLoss();
                        ib3.setBackgroundColor(Color.WHITE);
                        ib2.setBackgroundColor(R.color.colorfragment);
                        ib1.setBackgroundColor(R.color.colorfragment);

                    default:

                        break;

                }
            }
        };
        ib1.setOnClickListener(listener);
        ib2.setOnClickListener(listener);
        ib3.setOnClickListener(listener);

    }
}
