package com.example.smartcampus.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcampus.R;
import com.example.smartcampus.db.UserCampusDatabaseHelper;
import com.example.smartcampus.db.UserDatabaseHelper;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.entity.UserCampus;
import com.example.smartcampus.util.Result;
import com.example.smartcampus.util.Server;
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MajorActivity extends AppCompatActivity {
    private UserDatabaseHelper userDatabaseHelper;
    private SQLiteDatabase userDb;
    private UserCampusDatabaseHelper userCampusDatabaseHelper;
    private SQLiteDatabase userCampusDb;
    private TextView et_major;
    private TextView tv_save;
    private User user;
    private UserCampus userCampus;
    public void initView() {
        et_major = findViewById(R.id.et_major);
        tv_save = findViewById(R.id.tv_save);
        user = new User();
        userDatabaseHelper = new UserDatabaseHelper(this, "smartcampus.db", null, 1);
        userDb = userDatabaseHelper.getWritableDatabase();
        userDatabaseHelper.query(userDb);
        user = userDatabaseHelper.getUser();
        userCampusDatabaseHelper = new UserCampusDatabaseHelper(this, "smartcampus.db", null, 1);
        userCampusDb = userCampusDatabaseHelper.getWritableDatabase();
        userCampusDatabaseHelper.query(userCampusDb);
        userCampus = userCampusDatabaseHelper.getUserCampus();
        if (userCampus.getMajor() != null) {
            et_major.setText(userCampus.getMajor());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);
        initView();
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        userCampus.setMajor(et_major.getText().toString());
                        Gson gson = new Gson();
                        Connection connection1 = Jsoup.connect(Server.Ip + ":8000/personalDetail");
                        Connection connection = Jsoup.connect(Server.Ip + ":8000/major");
                        Map<String, String> data = new HashMap<>();
                        data.put("email", user.getEmail());
                        System.out.println(user);
                        try {
                            Connection.Response response1 = connection1.method(Connection.Method.GET).data(data).ignoreContentType(true).timeout(Server.timeOut).execute();
                            Result result1 = gson.fromJson(response1.body(), Result.class);
                            if (result1.isSuccess()) {//获取个人信息成功！
                                user = gson.fromJson(gson.toJson(result1.getData()), User.class);
                                userDatabaseHelper.update(userDb, user);
                                user.setUserCampus(userCampus);
                                String stringData = gson.toJson(user);
                                Connection.Response response = connection.method(Connection.Method.PUT).header("Content-Type", "application/json").ignoreContentType(true).requestBody(stringData).timeout(Server.timeOut).execute();
                                Result result = gson.fromJson(response.body(), Result.class);
                                if (result.isSuccess()) {
                                    //更新本地数据库
                                    userCampusDatabaseHelper.updateMajor(userDb, userCampus);
                                    finish();
                                } else {
                                    System.out.println(result.getMsg());
                                    Looper.prepare();
                                    Toast.makeText(MajorActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } else {
                                System.out.println(result1.getMsg());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Looper.prepare();
                            Toast.makeText(MajorActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }).start();
            }
        });
    }
}
