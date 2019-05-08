package com.example.smartcampus.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.smartcampus.R;
import com.example.smartcampus.adapter.AttendCampusTimeAdapter;
import com.example.smartcampus.adapter.CampusAdapter;
import com.example.smartcampus.db.CampusDatabaseHelper;
import com.example.smartcampus.db.UserCampusDatabaseHelper;
import com.example.smartcampus.db.UserDatabaseHelper;
import com.example.smartcampus.entity.Campus;
import com.example.smartcampus.entity.MyDate;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.entity.UserCampus;
import com.example.smartcampus.util.Result;
import com.example.smartcampus.util.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendCampusTimeActivity extends AppCompatActivity {
    private ListView lv_attend_campus_time;
    private UserDatabaseHelper userDatabaseHelper;
    private CampusDatabaseHelper campusDatabaseHelper;
    private UserCampusDatabaseHelper userCampusDatabaseHelper;
    private SQLiteDatabase userDb;
    private SQLiteDatabase campusDb;
    private SQLiteDatabase userCampusDb;
    private User user;
    private List<String> yearList;
    int attendCampusTimePosition;
    private final Handler campusHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                AttendCampusTimeAdapter attendCampusTimeAdapter;
                if (user.getUserCampus() != null) {
                    attendCampusTimeAdapter = new AttendCampusTimeAdapter(AttendCampusTimeActivity.this, R.layout.attend_campus_time_item, yearList, user.getUserCampus().getAttendCampusTime());
                } else {
                    attendCampusTimeAdapter = new AttendCampusTimeAdapter(AttendCampusTimeActivity.this, R.layout.attend_campus_time_item, yearList, null);
                }
                lv_attend_campus_time.setAdapter(attendCampusTimeAdapter);
            } else if (message.what == 0) {
                AttendCampusTimeAdapter attendCampusTimeAdapter;
                if (user.getUserCampus() != null) {
                    yearList.add(user.getUserCampus().getAttendCampusTime());
                    attendCampusTimeAdapter = new AttendCampusTimeAdapter(AttendCampusTimeActivity.this, R.layout.attend_campus_time_item, yearList, user.getUserCampus().getAttendCampusTime());
                    lv_attend_campus_time.setAdapter(attendCampusTimeAdapter);
                }

            }
        }
    };

    public void initView() {
        lv_attend_campus_time = findViewById(R.id.lv_attend_campus_time);
        user = new User();
        userDatabaseHelper = new UserDatabaseHelper(this, "smartcampus.db", null, 1);
        userDb = userDatabaseHelper.getWritableDatabase();

        campusDatabaseHelper = new CampusDatabaseHelper(this, "smartcampus.db", null, 1);
        campusDb = campusDatabaseHelper.getWritableDatabase();

        userCampusDatabaseHelper = new UserCampusDatabaseHelper(this, "smartcampus.db", null, 1);
        userCampusDb = userCampusDatabaseHelper.getWritableDatabase();
        yearList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_campus_time);
        initView();
        userDatabaseHelper.query(userDb);
        user = userDatabaseHelper.getUser();
        if (userCampusDatabaseHelper.query(userCampusDb)) {
            campusDatabaseHelper.query(campusDb);
            user.setCampus(campusDatabaseHelper.getCampus());
            userCampusDatabaseHelper.query(userCampusDb);
            user.setUserCampus(userCampusDatabaseHelper.getUserCampus());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(Server.Ip + ":8000/personalDetail");
                Map<String, String> data = new HashMap<>();
                data.put("email", user.getEmail());
                try {
                    Connection.Response response = connection.method(Connection.Method.GET).data(data).ignoreContentType(true).timeout(Server.timeOut).execute();
                    Gson gson = new Gson();
                    Result result = gson.fromJson(response.body(), Result.class);
                    if (result.isSuccess()) {//获取云端个人信息成功！
                        user = gson.fromJson(gson.toJson(result.getData()), User.class);
                        Connection connection1 = Jsoup.connect(Server.Ip + ":8000/getDate");
                        Connection.Response response1 = connection1.method(Connection.Method.GET).ignoreContentType(true).timeout(Server.timeOut).execute();
                        Result result1 = gson.fromJson(response1.body(), Result.class);
                        if (result1.isSuccess()) {//获取服务器时间成功！
                            //TODO 通知主线程设置ListView配置器
                            MyDate myDate = gson.fromJson(gson.toJson(result1.getData()), MyDate.class);
                            for (int i = 0; i < 9; i++) {
                                if (myDate.getMonth() >= 6) {
                                    yearList.add(String.valueOf(myDate.getYear() - i));
                                } else {
                                    yearList.add(String.valueOf(myDate.getYear() - i-1));
                                }
                            }
                            Message message = campusHandler.obtainMessage();
                            message.what = 1;
                            campusHandler.sendMessage(message);
                        }
                    } else {
                        //TODO 本地有数据则使用本地数据配置ListView，否则什么都不显示
                        Message message = campusHandler.obtainMessage();
                        message.what = 0;
                        campusHandler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = campusHandler.obtainMessage();
                    message.what = 0;
                    campusHandler.sendMessage(message);
                }
            }
        }).start();

        lv_attend_campus_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                attendCampusTimePosition = position;
                // TODO 添加选择入学时间处理代码
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection connection = Jsoup.connect(Server.Ip + ":8000/personalDetail");
                        Connection connection1 = Jsoup.connect(Server.Ip + ":8000/attendCampusTime");
                        Gson gson = new Gson();
                        Map<String, String> data = new HashMap<>();
                        data.put("email", user.getEmail());
                        try {
                            Connection.Response response = connection.method(Connection.Method.GET).data(data).ignoreContentType(true).timeout(Server.timeOut).execute();
                            Result result = gson.fromJson(response.body(), Result.class);
                            if (result.isSuccess()) {
                                user = gson.fromJson(gson.toJson(result.getData()), User.class);
                                if (user.getUserCampus() != null) {
                                    user.getUserCampus().setAttendCampusTime(yearList.get(attendCampusTimePosition));
                                    String stringData = gson.toJson(user);
                                    Connection.Response response1 = connection1.method(Connection.Method.PUT).header("Content-Type", "application/json; charset=UTF-8").requestBody(stringData).ignoreContentType(true).timeout(Server.timeOut).execute();
                                    Result result1 = gson.fromJson(response1.body(), Result.class);
                                    if (result1.isSuccess()) {//设置入学时间成功，更新本地数据
                                        userDatabaseHelper.update(userDb, user);
                                        campusDatabaseHelper.update(campusDb, user.getCampus());
                                        userCampusDatabaseHelper.updateAttendCampusTime(userCampusDb, user.getUserCampus());
                                        finish();
                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(AttendCampusTimeActivity.this, result1.getMsg(), Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(AttendCampusTimeActivity.this, "请先设置学校！", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } else {
                                Looper.prepare();
                                Toast.makeText(AttendCampusTimeActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Looper.prepare();
                            Toast.makeText(AttendCampusTimeActivity.this, "服务器连接失败！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }).start();
//                Toast.makeText(CampusActivity.this, campusList.get(position).getCampusName(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
