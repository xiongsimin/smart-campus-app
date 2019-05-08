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
import com.example.smartcampus.util.Curriculum;

import java.util.List;

public class SecondhandMarketTabsAdapter extends ArrayAdapter {
    private final int resourceId;

    public SecondhandMarketTabsAdapter(@NonNull Context context, int resource, List<String> tabList) {
        super(context, resource, tabList);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String tabContent = (String) getItem(position);//获取当前项
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView tv_tab = (TextView) view.findViewById(R.id.tv_tab);
        tv_tab.setText(tabContent);//设置
        return view;
    }
}
