package com.example.smartcampus.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcampus.R;
import com.example.smartcampus.db.UserDatabaseHelper;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.util.Result;
import com.example.smartcampus.util.Server;
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NicknameActivity extends AppCompatActivity {
    private UserDatabaseHelper userDatabaseHelper;
    private SQLiteDatabase db;
    private User user;
    private EditText et_nickname;
    private TextView tv_save;

    public void initView() {
        user = new User();
        userDatabaseHelper = new UserDatabaseHelper(this, "smartcampus.db", null, 1);
        db = userDatabaseHelper.getWritableDatabase();
        userDatabaseHelper.query(db);
        user = userDatabaseHelper.getUser();
        et_nickname = findViewById(R.id.et_nickname);
        tv_save = findViewById(R.id.tv_save);
        et_nickname.setText(user.getNickname());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);
        initView();
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        user.setNickname(et_nickname.getText().toString());
                        Gson gson = new Gson();
                        Connection connection = Jsoup.connect(Server.Ip + ":8000/nickname");
                        String stringData = gson.toJson(user);
                        try {
                            Connection.Response response = connection.method(Connection.Method.PUT).header("Content-Type", "application/json").ignoreContentType(true).requestBody(stringData).timeout(Server.timeOut).execute();
                            Result result = gson.fromJson(response.body(), Result.class);
                            if (result.isSuccess()) {
                                //更新本地数据库
                                userDatabaseHelper.updateNickname(db, user);
                                finish();
                            } else {
                                System.out.println(result.getMsg());
                                Looper.prepare();
                                Toast.makeText(NicknameActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Looper.prepare();
                            Toast.makeText(NicknameActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }).start();
            }
        });
    }
}
