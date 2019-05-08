package com.example.smartcampus.activity;

import android.app.job.JobScheduler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.smartcampus.R;
import com.example.smartcampus.adapter.CampusAdapter;
import com.example.smartcampus.db.CampusDatabaseHelper;
import com.example.smartcampus.db.UserCampusDatabaseHelper;
import com.example.smartcampus.db.UserDatabaseHelper;
import com.example.smartcampus.entity.Campus;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.util.Result;
import com.example.smartcampus.util.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusActivity extends AppCompatActivity {
    private ListView lv_campus;
    private UserDatabaseHelper userDatabaseHelper;
    private CampusDatabaseHelper campusDatabaseHelper;
    private UserCampusDatabaseHelper userCampusDatabaseHelper;
    private SQLiteDatabase userDb;
    private SQLiteDatabase campusDb;
    private SQLiteDatabase userCampusDb;
    private User user;
    private List<Campus> campusList;
    int campusListPosition;
    private final Handler campusHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                CampusAdapter campusAdapter;
                if (user.getUserCampus() != null) {
                    campusAdapter = new CampusAdapter(CampusActivity.this, R.layout.campus_item, campusList, user.getCampus().getCampusName());
                } else {
                    campusAdapter = new CampusAdapter(CampusActivity.this, R.layout.campus_item, campusList, null);
                }
                lv_campus.setAdapter(campusAdapter);
            } else if (message.what == 0) {
                CampusAdapter campusAdapter;
                if (user.getUserCampus() != null) {
                    System.out.println("1122");
                    campusList.add(user.getCampus());
                    campusAdapter = new CampusAdapter(CampusActivity.this, R.layout.campus_item, campusList, user.getCampus().getCampusName());
                    lv_campus.setAdapter(campusAdapter);
                }

            }
        }
    };

    public void initView() {
        lv_campus = findViewById(R.id.lv_campus);
        user = new User();
        userDatabaseHelper = new UserDatabaseHelper(this, "smartcampus.db", null, 1);
        userDb = userDatabaseHelper.getWritableDatabase();

        campusDatabaseHelper = new CampusDatabaseHelper(this, "smartcampus.db", null, 1);
        campusDb = campusDatabaseHelper.getWritableDatabase();

        userCampusDatabaseHelper = new UserCampusDatabaseHelper(this, "smartcampus.db", null, 1);
        userCampusDb = userCampusDatabaseHelper.getWritableDatabase();
        campusList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus);
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
                        Connection connection1 = Jsoup.connect(Server.Ip + ":8000/campus");
                        Connection.Response response1 = connection1.method(Connection.Method.GET).ignoreContentType(true).timeout(Server.timeOut).execute();
                        Result result1 = gson.fromJson(response1.body(), Result.class);
                        if (result1.isSuccess()) {//获取云端学校信息成功！
                            //TODO 通知主线程设置ListView配置器
                            String stringCampusList = gson.toJson(result1.getData());
                            System.out.println(stringCampusList);
                            Type campusListType = new TypeToken<List<Campus>>() {
                            }.getType();
                            campusList = gson.fromJson(stringCampusList, campusListType);
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

        lv_campus.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                campusListPosition = position;
                // TODO 添加选择学校处理代码
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection connection = Jsoup.connect(Server.Ip + ":8000/userCampus");
                        String stringData;
                        Gson gson = new Gson();
                        if (user.getCampus() != null) {
                            user.getCampus().setCampusName(campusList.get(campusListPosition).getCampusName());
                            stringData = gson.toJson(user);
                        } else {
                            Campus campus = new Campus();
                            campus.setCampusName(campusList.get(campusListPosition).getCampusName());
                            user.setCampus(campus);
                            stringData = gson.toJson(user);
                        }
                        try {
                            Connection.Response response = connection.method(Connection.Method.POST).header("Content-Type", "application/json; charset=UTF-8").ignoreContentType(true).requestBody(stringData).timeout(Server.timeOut).execute();
                            Result result = gson.fromJson(response.body(), Result.class);
                            if (result.isSuccess()) {
                                //TODO 更新本地数据库
                                Connection connection1 = Jsoup.connect(Server.Ip + ":8000/personalDetail");
                                Map<String,String> data=new HashMap<>();
                                data.put("email",user.getEmail());
                                Connection.Response response1 = connection1.method(Connection.Method.GET).data(data).ignoreContentType(true).timeout(Server.timeOut).execute();
                                Result result1 = gson.fromJson(response1.body(), Result.class);
                                if(result1.isSuccess()){
                                    user=gson.fromJson(gson.toJson(result1.getData()),User.class);
                                    if(userCampusDatabaseHelper.query(userCampusDb)){
                                        userDatabaseHelper.update(userDb, user);
                                        campusDatabaseHelper.update(campusDb, user.getCampus());
                                        userCampusDatabaseHelper.update(userCampusDb, user.getUserCampus());
                                    }else {
                                        userDatabaseHelper.update(userDb, user);
                                        campusDatabaseHelper.insert(campusDb, user.getCampus());
                                        userCampusDatabaseHelper.insert(userCampusDb, user.getUserCampus());
                                    }
                                    userDatabaseHelper.query(userDb);
                                    System.out.println(userDatabaseHelper.getUser());
                                    campusDatabaseHelper.query(campusDb);
                                    System.out.println(campusDatabaseHelper.getCampus());
                                    userCampusDatabaseHelper.query(userCampusDb);
                                    System.out.println(userCampusDatabaseHelper.getUserCampus());
                                }else {
                                    System.out.println(result1.getMsg());
                                }
                                finish();
                            } else {
                                Toast.makeText(CampusActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
//                Toast.makeText(CampusActivity.this, campusList.get(position).getCampusName(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
