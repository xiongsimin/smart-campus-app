package com.example.smartcampus.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.smartcampus.R;

public class CurriculumScheduleSettingActivity extends AppCompatActivity {
    private TextView tv_present_term;
    private TextView tv_persent_week;

    public void initView() {
        tv_present_term = findViewById(R.id.tv_present_term);
        tv_persent_week = findViewById(R.id.tv_present_week);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculum_schedule_setting);

        initView();
        tv_present_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurriculumScheduleSettingActivity.this, TermActivity.class);
                startActivity(intent);
            }
        });
        tv_persent_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 弹出周数选择对话框
                
            }
        });
    }
}
