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
import com.example.smartcampus.util.Curriculum;

import java.util.List;

public class CampusAdapter extends ArrayAdapter {
    private final int resourceId;
    private String campusName;
    public CampusAdapter(@NonNull Context context, int resource, List<Campus> campusList,String campusName) {
        super(context, resource,campusList);
        resourceId=resource;
        this.campusName=campusName;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Campus campus=(Campus)getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView tv_campus_item = (TextView) view.findViewById(R.id.tv_campus_item);
        tv_campus_item.setText(campus.getCampusName());
        if(campusName!=null){
            if(campus.getCampusName().equals(campusName)){//默认选中
                tv_campus_item.setBackground(tv_campus_item.getResources().getDrawable(R.drawable.ic_bottom_line_checked));
            }
        }
        return view;
    }
}
