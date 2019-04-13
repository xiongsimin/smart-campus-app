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
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetPasswordActivity extends AppCompatActivity {
    private EditText et_password;
    private EditText et_confirm_password;
    private Button bt_save;

    public void initView() {
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        bt_save = findViewById(R.id.bt_save);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        initView();
        final Intent intent=getIntent();//接收上个页面传来的参数email
        final String email=intent.getStringExtra("email");
        //点击保存
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断密码是否非空
                if (!et_password.getText().toString().trim().equals("")) {
                    //判断密码是否合法【初步只考察是否存在空格】
                    if (et_password.getText().toString().equals(et_password.getText().toString().trim())) {
                        //判断确认密码是否非空
                        if (!et_confirm_password.getText().toString().trim().equals("")) {
                            //判断密码和确认密码是否一致
                            if(et_password.getText().toString().trim().equals(et_confirm_password.getText().toString().trim())){
                                //发送请求设置密码并跳转到登录页面
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Connection connection= Jsoup.connect(Server.Ip+":8000/register");
                                        Map<String,String> data=new HashMap<>();
                                        data.put("email",email);
                                        data.put("password",et_password.getText().toString().trim());
                                        Gson gson=new Gson();
                                        String stringData=gson.toJson(data);
                                        try {
                                            Connection.Response response=connection.method(Connection.Method.PUT).header("Content-Type","application/json").requestBody(stringData).ignoreContentType(true).timeout(Server.timeOut).execute();
                                            String resultString = response.body();
                                            Result rs = gson.fromJson(resultString, Result.class);
                                            if(rs.isSuccess()){
                                                Looper.prepare();
                                                Toast.makeText(SetPasswordActivity.this, "密码设置成功！请登录！", Toast.LENGTH_SHORT).show();
                                                Intent intent1=new Intent(SetPasswordActivity.this,LoginActivity.class);
                                                startActivity(intent1);
                                                Looper.loop();
                                            }else {//提示错误信息
                                                Looper.prepare();
                                                Toast.makeText(SetPasswordActivity.this, rs.getMsg(), Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }
                                        } catch (IOException e) {//网络请求异常
                                            Looper.prepare();
                                            Toast.makeText(SetPasswordActivity.this, "连接服务器失败！请重试！", Toast.LENGTH_LONG).show();
                                            Looper.loop();
                                            e.printStackTrace();
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }else {
                                Toast.makeText(SetPasswordActivity.this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SetPasswordActivity.this, "请输入确认密码！", Toast.LENGTH_SHORT).show();
                            et_confirm_password.requestFocus();//确认密码输入框获取焦点
                            et_confirm_password.setSelection(et_confirm_password.getText().length());//确认密码输入框光标指向尾部
                        }
                    }else {
                        Toast.makeText(SetPasswordActivity.this, "密码中不能包含空格！", Toast.LENGTH_SHORT).show();
                        et_password.requestFocus();//密码输入框获取焦点
                        et_password.setSelection(et_password.getText().length());//密码输入框光标指向尾部
                    }
                } else {
                    Toast.makeText(SetPasswordActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                    et_password.requestFocus();//密码输入框获取焦点
                    et_password.setSelection(et_password.getText().length());//密码输入框光标指向尾部
                }
            }
        });
    }
}
