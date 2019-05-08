package com.example.smartcampus.storage;

import android.content.Context;

import com.example.smartcampus.entity.User;
import com.example.smartcampus.util.Result;
import com.google.gson.Gson;

import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LocalStorage {
    /**
     * 从本地文件中获取用户详细信息
     * @param context
     * @return
     */
    public Result getUserDetails(Context context) {
        Result rs=new Result();
        FileInputStream fis = null;
        String userString="";
        User user=new User();
        try {
            fis = context.openFileInput("userDetails");
            StringBuilder sb = new StringBuilder();
            int temp = 0;
            //当temp=-1 说明已经到了文件末尾
            while ((temp = fis.read()) != -1) {
                sb.append((char) temp);
            }
            userString = sb.toString();
            fis.close();
            Gson gson=new Gson();
            user=gson.fromJson(userString,User.class);
            rs.setSuccess(true);
            rs.setData(user);
        } catch (FileNotFoundException e) {
            System.out.println("用户详情文件不存在！");
            rs.setSuccess(false);
            rs.setMsg("用户详情文件不存在！");
            e.printStackTrace();
        } catch (IOException e) {
            rs.setSuccess(false);
            rs.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }
}

