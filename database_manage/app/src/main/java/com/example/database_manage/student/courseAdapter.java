package com.example.database_manage.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.example.database_manage.R;

import java.util.List;

/*
主要用于设定listview的适配器
 */
public class courseAdapter extends BaseAdapter {
    Context mContext;
    List<item> mList;
    ViewHolder mViewHolder;

    public courseAdapter(Context mContext, List<item> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final item it = mList.get(i);
        if (view == null) {
            //用LayouInflater加载布局，传给布局对象view
            // 用view找到三个控件，存放在viewHolder中，再把viewHolder储存到View中
            // 完成了把控件展示在ListView的步骤
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_course, viewGroup, false);

            mViewHolder = new ViewHolder();
            mViewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox_1);
            mViewHolder.course_name = (TextView) view.findViewById(R.id.t_course_name);
            mViewHolder.course_time = (TextView) view.findViewById(R.id.t_course_time);
            mViewHolder.course_period = (TextView) view.findViewById(R.id.t_course_period);
            mViewHolder.teacher_name = view.findViewById(R.id.t_teacher_name);
            mViewHolder.course_weight = view.findViewById(R.id.t_course_weight);
            view.setTag(mViewHolder);
        } else {

            mViewHolder = (ViewHolder) view.getTag();
        }
        mViewHolder.checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        it.setIscheck(b);
                    }

                });
        mViewHolder.course_name.setText(it.getCourse_name());
        mViewHolder.course_time.setText(it.getCourse_time());
        mViewHolder.course_period.setText(it.getCourse_period());
        mViewHolder.teacher_name.setText(it.getTeacher_name());
        mViewHolder.checkBox.setChecked(it.getIscheck());
        mViewHolder.course_weight.setText(it.getCourse_weight());
        return view;
    }

    class ViewHolder {
        TextView course_name;
        TextView course_time;
        CheckBox checkBox;
        TextView course_period;
        TextView teacher_name;
        TextView course_weight;
    }
}



