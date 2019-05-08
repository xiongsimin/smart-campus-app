package com.example.smartcampus.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcampus.R;
import com.example.smartcampus.adapter.TermAdapter;
import com.example.smartcampus.db.CampusDatabaseHelper;
import com.example.smartcampus.db.TermDatabaseHelper;
import com.example.smartcampus.db.UserCampusDatabaseHelper;
import com.example.smartcampus.db.UserDatabaseHelper;
import com.example.smartcampus.entity.MyDate;
import com.example.smartcampus.entity.Term;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.fragment.CurriculumScheduleFragment;
import com.example.smartcampus.util.Result;
import com.example.smartcampus.util.Server;
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TermActivity extends AppCompatActivity {
    private ListView lv_term;
    private List<Term> termList;
    private TextView tv_add_term;
    private Term term;
    private List<Term> newTermList;
    private TermDatabaseHelper termDatabaseHelper;
    private UserDatabaseHelper userDatabaseHelper;
    private CampusDatabaseHelper campusDatabaseHelper;
    private UserCampusDatabaseHelper userCampusDatabaseHelper;
    private SQLiteDatabase termDb;
    private SQLiteDatabase userDb;
    private SQLiteDatabase campusDb;
    private SQLiteDatabase userCampusDb;
    private User user;


    private final Handler termHandler = new Handler() {
        public void handleMessage(Message message) {
            if (termDatabaseHelper.queryAll(termDb)) {//本地存在已创建的学期
                termList = termDatabaseHelper.getTermList();
                if (termDatabaseHelper.query(termDb)) {//查询已选中的学期
                    term = termDatabaseHelper.getTerm();
                }
                TermAdapter termAdapter = new TermAdapter(TermActivity.this, R.layout.term_item, termList, term);
                lv_term.setAdapter(termAdapter);
            } else {
                System.out.println("本地不存在已创建的学期");
            }

        }
    };
    private final Handler newTermHandler = new Handler() {
        public void handleMessage(Message message) {
            final String[] items = new String[newTermList.size()];
            for (int i = 0; i < newTermList.size(); i++) {
                if (newTermList.get(i).getYear() - Integer.parseInt(user.getUserCampus().getAttendCampusTime()) >= 0 && newTermList.get(i).getYear() - Integer.parseInt(user.getUserCampus().getAttendCampusTime()) < 4) {
                    if (newTermList.get(i).getYear() - Integer.parseInt(user.getUserCampus().getAttendCampusTime()) == 0) {
                        items[i] = newTermList.get(i).getYear() + "-" + (newTermList.get(i).getYear() + 1) + "学年 第" + newTermList.get(i).getWhichTerm() + "学期（大一）";
                    } else if (newTermList.get(i).getYear() - Integer.parseInt(user.getUserCampus().getAttendCampusTime()) == 1) {
                        items[i] = newTermList.get(i).getYear() + "-" + (newTermList.get(i).getYear() + 1) + "学年 第" + newTermList.get(i).getWhichTerm() + "学期（大二）";
                    } else if (newTermList.get(i).getYear() - Integer.parseInt(user.getUserCampus().getAttendCampusTime()) == 2) {
                        items[i] = newTermList.get(i).getYear() + "-" + (newTermList.get(i).getYear() + 1) + "学年 第" + newTermList.get(i).getWhichTerm() + "学期（大三）";
                    } else if (newTermList.get(i).getYear() - Integer.parseInt(user.getUserCampus().getAttendCampusTime()) == 3) {
                        items[i] = newTermList.get(i).getYear() + "-" + (newTermList.get(i).getYear() + 1) + "学年 第" + newTermList.get(i).getWhichTerm() + "学期（大四）";
                    }
                } else {
                    items[i] = newTermList.get(i).getYear() + "-" + (newTermList.get(i).getYear() + 1) + "学年 第" + newTermList.get(i).getWhichTerm() + "学期";
                }
            }
            AlertDialog.Builder alertDialog =
                    new AlertDialog.Builder(TermActivity.this);
            alertDialog.setTitle("请选择学期");
            alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(TermActivity.this, items[which], Toast.LENGTH_SHORT).show();
                    Term term = new Term();
                    int year = Integer.parseInt(items[which].substring(0, items[which].indexOf("-")));
                    int whichTerm = Integer.parseInt(items[which].substring(items[which].indexOf("第") + "第".length(), items[which].indexOf("学期")));
                    term.setYear(year);
                    term.setWhichTerm(whichTerm);
                    if (termDatabaseHelper.queryByTerm(termDb, term)) {//已经存在该学期
                        Toast.makeText(TermActivity.this, "已经存在该学期！", Toast.LENGTH_SHORT).show();
                    } else {
                        termDatabaseHelper.insert(termDb, term);
                        Message message1 = termHandler.obtainMessage();
                        termHandler.sendMessage(message1);
                    }
                }
            });
            alertDialog.show();
        }
    };

    public void initView() {
        lv_term = findViewById(R.id.lv_term);
        tv_add_term = findViewById(R.id.tv_add_term);
        termList = new ArrayList<>();
        newTermList = new ArrayList<>();
        term = new Term();
        user = new User();
        termDatabaseHelper = new TermDatabaseHelper(TermActivity.this, "smartcampus.db", null, 1);
        userDatabaseHelper = new UserDatabaseHelper(TermActivity.this, "smartcampus.db", null, 1);
        campusDatabaseHelper = new CampusDatabaseHelper(TermActivity.this, "smartcampus.db", null, 1);
        userCampusDatabaseHelper = new UserCampusDatabaseHelper(TermActivity.this, "smartcampus.db", null, 1);
        termDb = termDatabaseHelper.getWritableDatabase();
        userDb = userDatabaseHelper.getWritableDatabase();
        campusDb = campusDatabaseHelper.getWritableDatabase();
        userCampusDb = userCampusDatabaseHelper.getWritableDatabase();

        userDatabaseHelper.query(userDb);
        user = userDatabaseHelper.getUser();
        if (userCampusDatabaseHelper.query(userCampusDb)) {
            campusDatabaseHelper.query(campusDb);
            user.setCampus(campusDatabaseHelper.getCampus());
            userCampusDatabaseHelper.query(userCampusDb);
            user.setUserCampus(userCampusDatabaseHelper.getUserCampus());
        }
        /*termList.add(new Term(2017, 1));
        termList.add(new Term(2016, 2));
        termList.add(new Term(2016, 1));
        termList.add(new Term(2015, 2));
        termList.add(new Term(2015, 1));

        term = new Term(2015, 2);*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        initView();
        tv_add_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                if (user.getUserCampus() != null) {
                                    if (user.getUserCampus().getAttendCampusTime() != null && user.getUserCampus().getAttendCampusTime().length() > 1) {
                                        Connection connection1 = Jsoup.connect(Server.Ip + ":8000/getDate");
                                        Connection.Response response1 = connection1.method(Connection.Method.GET).ignoreContentType(true).timeout(Server.timeOut).execute();
                                        Result result1 = gson.fromJson(response1.body(), Result.class);
                                        if (result1.isSuccess()) {//获取服务器时间成功！
                                            //TODO 通知主线程弹出对话框
                                            MyDate myDate = gson.fromJson(gson.toJson(result1.getData()), MyDate.class);
                                            newTermList.clear();
                                            for (int i = 0; i < 9; i++) {
                                                if (myDate.getMonth() >= 6) {
                                                    newTermList.add(new Term(myDate.getYear() - i, 2));
                                                    newTermList.add(new Term(myDate.getYear() - i, 1));
                                                } else {
                                                    newTermList.add(new Term(myDate.getYear() - 1 - i, 2));
                                                    newTermList.add(new Term(myDate.getYear() - 1 - i, 1));
                                                }
                                            }
                                            Message message = newTermHandler.obtainMessage();
                                            message.what = 1;
                                            newTermHandler.sendMessage(message);
                                        } else {
                                            Looper.prepare();
                                            Toast.makeText(TermActivity.this, result1.getMsg(), Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }
                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(TermActivity.this, "请先完善入学年份信息！", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(TermActivity.this, "请先完善学校信息及入学年份信息！", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } else {
                                //TODO 本地有数据则使用本地数据配置ListView，否则什么都不显示
                                Looper.prepare();
                                Toast.makeText(TermActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Looper.prepare();
                            Toast.makeText(TermActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }).start();
            }
        });
        Message message = termHandler.obtainMessage();
        termHandler.sendMessage(message);
    }
}
