package com.example.smartcampus.activity;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {
    private EditText et_email;
    private EditText et_checkCode;
    private Button bt_register;
    private Button bt_getCheckCode;

    public void initView() {
        et_email = findViewById(R.id.et_email);
        et_checkCode = findViewById(R.id.et_checkCode);
        bt_getCheckCode = findViewById(R.id.bt_getCheckCode);
        bt_register = findViewById(R.id.bt_register);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        //点击注册按钮
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证是否非空
                if (!et_email.getText().toString().trim().equals("")) {
                    //邮箱格式是否合法
                    if (ValidationUtil.isEmail(et_email.getText().toString().trim())) {
                        //验证验证码非空
                        if (!et_checkCode.getText().toString().trim().equals("")) {
                            //TODO 邮箱和验证码都合法，发送注册请求
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Connection connection = Jsoup.connect(Server.Ip + ":8000/register");
                                    Map<String, String> data = new HashMap<>();
                                    data.put("email", et_email.getText().toString().trim());
                                    data.put("checkCode", et_checkCode.getText().toString().trim());
                                    Gson gson = new Gson();
                                    String stringData = gson.toJson(data);
                                    try {
                                        Connection.Response response = connection.method(Connection.Method.POST).header("Content-Type", "application/json").requestBody(stringData).ignoreContentType(true).timeout(Server.timeOut).execute();
                                        String resultString = response.body();
                                        Result rs = gson.fromJson(resultString, Result.class);
                                        if (rs.isSuccess()) {//如果验证成功，跳转到密码设置页面,同时传递参数email
                                            Intent intent = new Intent(RegisterActivity.this, SetPasswordActivity.class);
                                            intent.putExtra("email", et_email.getText().toString().trim());
                                            startActivity(intent);
                                        } else {//提示错误信息
                                            Looper.prepare();
                                            Toast.makeText(RegisterActivity.this, rs.getMsg(), Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }
                                    } catch (IOException e) {//网络请求异常
                                        Looper.prepare();
                                        Toast.makeText(RegisterActivity.this, "连接服务器失败！请重试！", Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(RegisterActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
                            et_checkCode.requestFocus();//验证码输入框获取焦点
                            et_checkCode.setSelection(et_checkCode.getText().length());//验证码输入框光标指向末尾
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "邮箱格式有误！", Toast.LENGTH_SHORT).show();
                        et_email.requestFocus();//邮箱输入框获取焦点
                        et_email.setSelection(et_email.getText().length());//邮箱输入框光标指向末尾
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "邮箱不能为空！", Toast.LENGTH_SHORT).show();
                    et_email.requestFocus();//邮箱输入框获取焦点
                    et_email.setSelection(et_email.getText().length());//邮箱输入框光标指向末尾
                }
            }
        });
        //点击获取验证码按钮
        bt_getCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_email.getText().toString().trim().equals("")) {
                    //邮箱格式是否合法
                    if (ValidationUtil.isEmail(et_email.getText().toString().trim())) {
                        // 邮箱合法，发送获取验证码请求
                        new Thread(new Runnable() {
                            @Override
                            public void run() {//register?
                                Connection connection = Jsoup.connect(Server.Ip + ":8000/register?email=" + et_email.getText().toString().trim());
                                Gson gson = new Gson();
                                try {
                                    Connection.Response response = connection.method(Connection.Method.GET).ignoreContentType(true).timeout(Server.timeOut).execute();
                                    String resultString = response.body();
                                    Result rs = gson.fromJson(resultString, Result.class);
                                    if (rs.isSuccess()) {
                                        Looper.prepare();
                                        Toast.makeText(RegisterActivity.this, "验证码已发送至【" + et_email.getText().toString().trim() + "】请注意查收！", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(RegisterActivity.this, rs.getMsg(), Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                } catch (IOException e) {//网络连接失败
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this, "连接服务器失败！请重试！", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                    } else {
                        Toast.makeText(RegisterActivity.this, "邮箱格式有误！", Toast.LENGTH_SHORT).show();
                        et_email.requestFocus();//邮箱输入框获取焦点
                        et_email.setSelection(et_email.getText().length());//邮箱输入框光标指向末尾
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "邮箱不能为空！", Toast.LENGTH_SHORT).show();
                    et_email.requestFocus();//邮箱输入框获取焦点
                    et_email.setSelection(et_email.getText().length());//邮箱输入框光标指向末尾
                }
            }
        });
    }
}
