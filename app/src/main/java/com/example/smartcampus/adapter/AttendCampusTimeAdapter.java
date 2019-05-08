package com.example.smartcampus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.smartcampus.R;
import com.example.smartcampus.entity.Campus;

import java.util.List;

public class AttendCampusTimeAdapter extends ArrayAdapter {
    private final int resourceId;
    String year;
    public AttendCampusTimeAdapter(@NonNull Context context, int resource, List<String> yearList,String year) {
        super(context, resource,yearList);
        resourceId=resource;
        this.year=year;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String year=(String)getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView tv_attend_campus_time_item = (TextView) view.findViewById(R.id.tv_attend_campus_time_item);
        tv_attend_campus_time_item.setText(year);
        if(this.year!=null){
            if(year.equals(this.year)){//默认选中
                tv_attend_campus_time_item.setBackground(tv_attend_campus_time_item.getResources().getDrawable(R.drawable.ic_bottom_line_checked));
            }
        }
        return view;
    }
}
