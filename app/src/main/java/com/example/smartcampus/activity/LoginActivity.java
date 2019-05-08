package com.example.smartcampus.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcampus.R;
import com.example.smartcampus.db.CampusDatabaseHelper;
import com.example.smartcampus.db.UserCampusDatabaseHelper;
import com.example.smartcampus.db.UserDatabaseHelper;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.util.Result;
import com.example.smartcampus.util.Server;
import com.example.smartcampus.util.ValidationUtil;
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button bt_login;
    private EditText et_email;
    private EditText et_password;
    private TextView tv_register;
    UserDatabaseHelper userDatabaseHelper;
    SQLiteDatabase userDb;
    CampusDatabaseHelper campusDatabaseHelper;
    SQLiteDatabase campusDb;
    UserCampusDatabaseHelper userCampusDatabaseHelper;
    SQLiteDatabase userCampusDb;

    public void initView() {
        bt_login = findViewById(R.id.bt_login);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_register = findViewById(R.id.tv_register);
        userDatabaseHelper = new UserDatabaseHelper(LoginActivity.this, "smartcampus.db", null, 1);
        userDb = userDatabaseHelper.getWritableDatabase();

        campusDatabaseHelper = new CampusDatabaseHelper(LoginActivity.this, "smartcampus.db", null, 1);
        campusDb = campusDatabaseHelper.getWritableDatabase();

        userCampusDatabaseHelper = new UserCampusDatabaseHelper(LoginActivity.this, "smartcampus.db", null, 1);
        userCampusDb = userCampusDatabaseHelper.getWritableDatabase();
        //测试SQLLite

        /*UserDatabaseHelper userDatabaseHelper =new UserDatabaseHelper(this,"smartcampus.db",null,1);
        SQLiteDatabase db= userDatabaseHelper.getWritableDatabase();
        userDatabaseHelper.insert(db,user);
        userDatabaseHelper.query(db);
        UserDatabaseHelper bb=new UserDatabaseHelper(this,"smartcampus.db",null,1);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //TODO 判断本地数据库是否存在用户登录信息
        if(userDatabaseHelper.isExist(userDb)){
            if (userDatabaseHelper.query(userDb)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = userDatabaseHelper.getUser();
                        System.out.println(user);
                        Connection connection = Jsoup.connect(Server.Ip + ":8000/login");
                        Map<String, String> data = new HashMap<>();
                        data.put("email", user.getEmail());
                        data.put("password", user.getPassword());
                        Gson gson = new Gson();
                        String stringData = gson.toJson(data);
                        Connection.Response response = null;
                        try {
                            response = connection.method(Connection.Method.POST).requestBody(stringData).header("Content-Type", "application/json; charset=UTF-8").ignoreContentType(true).timeout(Server.timeOut).execute();
                            String resultString = response.body();
                            Result rs = gson.fromJson(resultString, Result.class);
                            if (rs.isSuccess()) {//登陆成功，跳转到首页，同时从服务器获取个人信息并更新本地数据库
                                connection = Jsoup.connect(Server.Ip + ":8000/personalDetail");
                                data = new HashMap<>();
                                data.put("email", user.getEmail());
                                response = connection.method(Connection.Method.GET).data(data).ignoreContentType(true).timeout(Server.timeOut).execute();
                                Result rs1 = gson.fromJson(response.body(), Result.class);
                                if (rs1.isSuccess()) {//从数据库获取个人信息成功，更新本地数据库
                                    String stringUser = gson.toJson(rs1.getData());
                                    user = gson.fromJson(stringUser, User.class);
                                    userDatabaseHelper.update(userDb, user);
                                } else {
                                    System.out.println(rs1.getMsg());
                                }
                                Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                                startActivity(intent);
                            } else {
                                System.out.println(rs.getMsg());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            // 为了测试方便 访问不了服务器时也跳到主页
                            Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                            startActivity(intent);
                        }
                    }
                }).start();
            } else {
                System.out.println("用户登录信息不存在！");
            }
        }else {
            System.out.println("表user不存在！");
        }

        /*FileInputStream fis = null;
        try {
            fis = openFileInput("userDetails");
            StringBuilder sb = new StringBuilder();
            int temp = 0;
            //当temp=-1 说明已经到了文件末尾
            while ((temp = fis.read()) != -1) {
                sb.append((char) temp);
            }
            Gson gson = new Gson();
            User user = gson.fromJson(sb.toString(), User.class);
            String userEmail = user.getEmail();
            fis = openFileInput("userPassword");
            sb = new StringBuilder();
            temp = 0;
            while ((temp = fis.read()) != -1) {
                sb.append((char) temp);
            }
            String userPassword = sb.toString();
            fis.close();
            if (!userEmail.equals("")) {
                System.out.println("用户邮箱是：" + userEmail);
                if (!userPassword.equals("")) {
                    System.out.println("用户密码是：" + userPassword);
                    //TODO 如果有网络 尝试登陆操作；如果没有网络，直接跳到主页； 暂时直接跳入主页
                    Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                    startActivity(intent);
                } else {
                    System.out.println("用户密码不存在！");
                }
            } else {
                System.out.println("用户邮箱不存在！");
            }
        } catch (FileNotFoundException e) {
            System.out.println("文件not found异常！！！");

            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO异常！！！");
            e.printStackTrace();
        }*/


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
                                Connection connection = Jsoup.connect(Server.Ip + ":8000/login");
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
                                    if (rs.isSuccess()) {//登陆成功，跳转到首页，同时从服务器获取个人信息并存入本地数据库
                                        String fileName1 = "userDetails";
                                        String fileName2 = "userPassword";
                                        String userEmail = et_email.getText().toString().trim();
                                        String userPassword = et_password.getText().toString();
                                        connection = Jsoup.connect(Server.Ip + ":8000/personalDetail");
                                        data = new HashMap<>();
                                        data.put("email", userEmail);
//                                        stringData = gson.toJson(data);
                                        response = connection.method(Connection.Method.GET).data(data).ignoreContentType(true).timeout(Server.timeOut).execute();
                                        Result rs1 = gson.fromJson(response.body(), Result.class);
                                        if (rs1.isSuccess()) {//如果从服务器成功获取到了用户详情，将用户详情存入本地
                                            System.out.println("从服务器端获取到的" + rs1.getData());
                                            String stringUser = gson.toJson(rs1.getData());
                                            User user = gson.fromJson(stringUser, User.class);
                                            userDatabaseHelper.create(userDb);
                                            campusDatabaseHelper.create(campusDb);
                                            userCampusDatabaseHelper.create(userCampusDb);

                                            userDatabaseHelper.insert(userDb, user);//user表添加用户数据
                                            if(user.getUserCampus()!=null){
                                                campusDatabaseHelper.insert(campusDb,user.getCampus());//campus表...
                                                userCampusDatabaseHelper.insert(userCampusDb,user.getUserCampus());//userCampus表...
                                            }
                                           /* FileOutputStream fos = openFileOutput(fileName1, MODE_PRIVATE);
                                            fos.write(gson.toJson(user).getBytes());
                                            fos = openFileOutput(fileName2, MODE_PRIVATE);
                                            fos.write(userPassword.getBytes());
                                            fos.close();*/

                                            /*///////////////////
                                            FileInputStream fis = openFileInput("userDetails");
                                            StringBuilder sb = new StringBuilder();
                                            int temp = 0;
                                            //当temp=-1 说明已经到了文件末尾
                                            while ((temp = fis.read()) != -1) {
                                                sb.append((char) temp);
                                            }
                                            String wewe = sb.toString();
                                            System.out.println("测试测试111"+wewe);
                                            //////////////////*/
                                        } else {
                                            System.out.println(rs1.getMsg());
                                        }
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
                                    /*// 为了测试方便 访问不了服务器时也跳到主页
                                    Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                                    startActivity(intent);*/
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
