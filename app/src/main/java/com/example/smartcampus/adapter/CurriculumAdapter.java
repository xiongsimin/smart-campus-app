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

import org.w3c.dom.Text;

import java.util.List;

public class CurriculumAdapter extends ArrayAdapter {
    private final int resourceId;
    private final Context myContext;

    public CurriculumAdapter(@NonNull Context context, int resource, List<Curriculum> curriculumList) {
        super(context, resource, curriculumList);
        resourceId = resource;
        myContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Curriculum curriculum = (Curriculum) getItem(position);//获取当前项的Curriculum实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView tv_curriculum = (TextView) view.findViewById(R.id.tv_curriculum);
        if (curriculum != null) {
            if (curriculum.getCurriculumName()!=null) {
                tv_curriculum.setText(curriculum.getCurriculumName()+"@"+curriculum.getClassRoom());
                tv_curriculum.setBackgroundColor(tv_curriculum.getResources().getColor(curriculum.getColor()));//设置课程背景颜色
                System.out.println("最终颜色代码"+tv_curriculum.getResources().getColor(curriculum.getColor()));
//            tv_curriculum.setBackgroundColor(tv_curriculum.getResources().getColor(R.color.colorLightBlue));//设置课程背景颜色

            }
        }
        return view;
    }
}
