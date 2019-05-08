package com.example.smartcampus.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartcampus.R;

public class ImportScheduleActivity extends AppCompatActivity {
    private TextView tv_term_info;
    private EditText et_campus_card_number;
    private EditText et_campus_card_password;
    private EditText et_check_code;
    private ImageView iv_check_code;
    private Button btn_get_schedule;

    private int year;
    private int whichTerm;


    public void initView() {
        tv_term_info = findViewById(R.id.tv_term_info);
        et_campus_card_number = findViewById(R.id.et_campus_card_number);
        et_campus_card_password = findViewById(R.id.et_campus_card_password);
        et_check_code = findViewById(R.id.et_check_code);
        iv_check_code = findViewById(R.id.iv_check_code);
        btn_get_schedule = findViewById(R.id.btn_get_schedule);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_schedule);

        initView();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        this.year = bundle.getInt("year");
        this.whichTerm = bundle.getInt("whichTerm");
        tv_term_info.setText(this.year + "-" + (this.year + 1) + "学年 第" + this.whichTerm + "学期");
    }
}
