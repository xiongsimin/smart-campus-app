package com.example.smartcampus.activity;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcampus.R;
import com.example.smartcampus.util.Result;
import com.example.smartcampus.util.Server;
import com.example.smartcampus.util.ValidationUtil;
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button bt_login;
    private EditText et_email;
    private EditText et_password;
    private TextView tv_register;

    public void initView() {
        bt_login = findViewById(R.id.bt_login);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_register=findViewById(R.id.tv_register);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //点击注册字样跳转到注册页
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //点击登录按钮
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //前端验证邮箱和密码合法性
                //1.验证邮箱格式合法性

                if (ValidationUtil.isEmail(et_email.getText().toString().trim())) {
                    //2.验证密码合法性(非空)

                    if (!et_password.getText().toString().trim().equals("")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Connection connection = Jsoup.connect(Server.Ip+":8000/login");
                                //Connection connection = Jsoup.connect("http://www.baidu.com");
                                Map<String, String> data = new HashMap<>();
                                data.put("email", et_email.getText().toString());
                                data.put("password", et_password.getText().toString());
                                Gson gson = new Gson();
                                String stringData = gson.toJson(data);
                                try {
                                    Connection.Response response = connection.method(Connection.Method.POST).requestBody(stringData).header("Content-Type", "application/json; charset=UTF-8").ignoreContentType(true).timeout(Server.timeOut).execute();
                                    String resultString = response.body();
                                    Result rs = gson.fromJson(resultString, Result.class);
                                    if (rs.isSuccess()) {
                                        Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(LoginActivity.this, rs.getMsg(), Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                } catch (IOException e) {//网络请求异常
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, "连接服务器失败！请重试！", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                    e.printStackTrace();

                                }
                            }
                        }).start();

                    } else {
                        Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                        et_password.requestFocus();//密码输入框获取焦点
                        et_password.setSelection(et_password.getText().length());//密码输入框光标指向末尾
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "邮箱格式有误！", Toast.LENGTH_SHORT).show();
                    et_email.requestFocus();//邮箱输入框获取焦点
                    et_email.setSelection(et_email.getText().length());//邮箱输入框光标指向末尾
                }
            }
        });
    }
}
