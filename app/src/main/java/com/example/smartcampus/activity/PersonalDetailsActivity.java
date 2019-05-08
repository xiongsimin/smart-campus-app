package com.example.smartcampus.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.smartcampus.R;
import com.example.smartcampus.db.CampusDatabaseHelper;
import com.example.smartcampus.db.UserCampusDatabaseHelper;
import com.example.smartcampus.db.UserDatabaseHelper;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.storage.LocalStorage;
import com.example.smartcampus.util.Result;
import com.example.smartcampus.util.Server;
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PersonalDetailsActivity extends AppCompatActivity {
    private TextView tv_back;
    private TextView tv_edit;
    private TextView tv_email;
    private TextView tv_nickname;
    private TextView tv_sex;
    private TextView tv_campus;
    private TextView tv_academy;
    private TextView tv_major;
    private TextView tv_degree;
    private TextView tv_attend_campus_time;
    UserDatabaseHelper userDatabaseHelper;
    SQLiteDatabase db;
    CampusDatabaseHelper campusDatabaseHelper;
    SQLiteDatabase campusDb;
    UserCampusDatabaseHelper userCampusDatabaseHelper;
    SQLiteDatabase userCampusDb;
    private final Handler personalCenterHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.arg1 == 1) {
                User user = (User) message.obj;
                tv_email.setText(user.getEmail());
                tv_nickname.setText(user.getNickname());
                tv_sex.setText(user.getSex());
                if (user.getUserCampus() != null) {
                    tv_campus.setText(user.getCampus().getCampusName());
                    tv_academy.setText(user.getUserCampus().getAcademy());
                    tv_major.setText(user.getUserCampus().getMajor());
                    tv_degree.setText(user.getUserCampus().getDegree());
                    tv_attend_campus_time.setText(user.getUserCampus().getAttendCampusTime());
                }
            } else {//从服务器获取用户信息失败,则暂时使用本地信息
                userDatabaseHelper.query(db);
                User user = userDatabaseHelper.getUser();
                System.out.println(user);
                if(userCampusDatabaseHelper.query(userCampusDb)){
                    campusDatabaseHelper.query(campusDb);
                    user.setCampus(campusDatabaseHelper.getCampus());
                    userCampusDatabaseHelper.query(userCampusDb);
                    user.setUserCampus(userCampusDatabaseHelper.getUserCampus());
                    tv_academy.setText(user.getUserCampus().getAcademy());
                    tv_major.setText(user.getUserCampus().getMajor());
                }
                /*LocalStorage localStorage = new LocalStorage();
                Result result = localStorage.getUserDetails(PersonalDetailsActivity.this);
                Gson gson = new Gson();
                String stringUser = gson.toJson(result.getData());
                User user = gson.fromJson(stringUser, User.class);*/
                tv_email.setText(user.getEmail());
                tv_nickname.setText(user.getNickname());
                tv_sex.setText(user.getSex());
                if (user.getUserCampus() != null) {
                    tv_campus.setText(user.getCampus().getCampusName());
                    tv_academy.setText(user.getUserCampus().getAcademy());
                    tv_major.setText(user.getUserCampus().getMajor());
                    tv_degree.setText(user.getUserCampus().getDegree());
                    tv_attend_campus_time.setText(user.getUserCampus().getAttendCampusTime());
                }
            }
        }
    };

    public void initView() {
        tv_back = findViewById(R.id.tv_back);
        tv_edit = findViewById(R.id.tv_edit);
        tv_email = findViewById(R.id.tv_email);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_sex = findViewById(R.id.tv_sex);
        tv_campus = findViewById(R.id.tv_campus);
        tv_academy = findViewById(R.id.tv_academy);
        tv_major = findViewById(R.id.tv_major);
        tv_degree = findViewById(R.id.tv_degree);
        tv_attend_campus_time = findViewById(R.id.tv_attend_campus_time);
        userDatabaseHelper = new UserDatabaseHelper(this, "smartcampus.db", null, 1);
        db = userDatabaseHelper.getWritableDatabase();

        campusDatabaseHelper = new CampusDatabaseHelper(this, "smartcampus.db", null, 1);
        campusDb = campusDatabaseHelper.getWritableDatabase();

        userCampusDatabaseHelper = new UserCampusDatabaseHelper(this, "smartcampus.db", null, 1);
        userCampusDb = userCampusDatabaseHelper.getWritableDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        initView();
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalDetailsActivity.this, EditPersonalDetailsActivity.class);
                startActivity(intent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                userDatabaseHelper.query(db);
                User user = userDatabaseHelper.getUser();
                /*LocalStorage localStorage = new LocalStorage();
                Result result = localStorage.getUserDetails(PersonalDetailsActivity.this);*/
                Connection connection = Jsoup.connect(Server.Ip + ":8000/personalDetail");
                Map<String, String> data = new HashMap<>();
                data.put("email", user.getEmail());
                Gson gson = new Gson();
//                    String stringData = gson.toJson(data);
                try {
                    Connection.Response response = connection.method(Connection.Method.GET).data(data).ignoreContentType(true).timeout(Server.timeOut).execute();
                    Result result1 = gson.fromJson(response.body(), Result.class);
                    if (result1.isSuccess()) {//如果从服务器成功获取到了用户详情，向主线程发送消息，参数arg1为1，obj为用户详情数据
                        System.out.println(result1.getData());
                        String stringUser = gson.toJson(result1.getData());
                        user = gson.fromJson(stringUser, User.class);
                        Message message = personalCenterHandler.obtainMessage();
                        message.arg1 = 1;
                        message.obj = user;
                        personalCenterHandler.sendMessage(message);
                    } else {//如果从服务器获取用户详情失败，向主线程发送消息，参数arg1为0
                        Message message = personalCenterHandler.obtainMessage();
                        message.arg1 = 0;
                        personalCenterHandler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = personalCenterHandler.obtainMessage();
                    message.arg1 = 0;
                    personalCenterHandler.sendMessage(message);
                }
            }
        }).start();
    }
}
