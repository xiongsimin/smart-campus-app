package com.example.smartcampus.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smartcampus.R;
import com.example.smartcampus.activity.EditPersonalDetailsActivity;
import com.example.smartcampus.activity.PersonalDetailsActivity;
import com.example.smartcampus.db.UserDatabaseHelper;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.storage.LocalStorage;
import com.example.smartcampus.util.CircleImageView;
import com.example.smartcampus.util.Result;
import com.example.smartcampus.util.Server;
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PersonalCenterFragment extends Fragment {
    private TextView tv_personal_details;
    private TextView tv_user_nickname;
    private CircleImageView civ_user_image;
    UserDatabaseHelper userDatabaseHelper;
    SQLiteDatabase db;

    public void initView() {
        tv_personal_details = getActivity().findViewById(R.id.tv_personal_details);
        tv_user_nickname = getActivity().findViewById(R.id.tv_user_nickname);
        civ_user_image=getActivity().findViewById(R.id.fragment_civ_user_image);
        userDatabaseHelper = new UserDatabaseHelper(getActivity(), "smartcampus.db", null, 1);
        db = userDatabaseHelper.getWritableDatabase();
    }

    private final Handler personalCenterHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.arg1 == 1) {
                User user = (User) message.obj;
                tv_user_nickname.setText(user.getNickname());
            } else {//从服务器获取用户信息失败,则暂时使用本地数据库信息
                userDatabaseHelper.query(db);
                User user = userDatabaseHelper.getUser();
                /*LocalStorage localStorage = new LocalStorage();
                Result result = localStorage.getUserDetails(getActivity());
                Gson gson = new Gson();
                String stringUser = gson.toJson(result.getData());
                User user = gson.fromJson(stringUser, User.class);*/
                tv_user_nickname.setText(user.getNickname());
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_center, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
//        Glide.with(getActivity()).load(Server.Ip+":80/1.png").into(civ_user_image);
        tv_personal_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalDetailsActivity.class);
                startActivity(intent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                userDatabaseHelper.query(db);
                User user = userDatabaseHelper.getUser();
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
                }
            }
        }).start();
        /*LocalStorage localStorage=new LocalStorage();
        Result result=localStorage.getUserDetails(getActivity());
        if(result.isSuccess()){

        }*/
    }
}
