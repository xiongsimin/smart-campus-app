package com.example.smartcampus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.smartcampus.entity.User;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private User user;

    public UserDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        user = new User();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void create(SQLiteDatabase db) {
        //创建表SQL语句
        String userTable = "create table if not exists user(_id integer,nickname text,sex text,email  text primary key,password text,realName text,idCardNumber text,realNameOrNot integer,userImagePath text)";
        //执行SQL语句
        db.execSQL(userTable);
    }

    public void insert(SQLiteDatabase db, User user) {
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //添加用户名
        cValue.put("_id", user.getId());
        cValue.put("nickname", user.getNickname());
        cValue.put("sex", user.getSex());
        cValue.put("email", user.getEmail());
        cValue.put("password", user.getPassword());
        cValue.put("realName", user.getRealName());
        cValue.put("idCardNumber", user.getIdCardNumber());
        cValue.put("realNameOrNot", user.getRealNameOrNot());
        cValue.put("userImagePath", user.getUserImagePath());
        //调用insert()方法插入数据
        db.insert("user", null, cValue);
    }

    public boolean query(SQLiteDatabase db) {
        user = new User();
        //查询获得游标
        Cursor cursor = db.query("user", null, null, null, null, null, null);

        //判断游标是否为空
        if (cursor.moveToFirst()) {
            /*//遍历游标
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.move(i);

                //获得昵称
                String nickname = cursor.getString(0);
                //获得性别
                String sex = cursor.getString(1);
                //输出用户信息
                System.out.println(nickname + ":" + sex);
            }*/
            cursor.move(0);
            user.setId(cursor.getLong(0));
            user.setNickname(cursor.getString(1));
            user.setSex(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            user.setRealName(cursor.getString(5));
            user.setIdCardNumber(cursor.getString(6));
            user.setRealNameOrNot(cursor.getInt(7));
            user.setUserImagePath(cursor.getString(8));
            return true;
        } else {
            System.out.println("本地数据库没有用户信息！");
            return false;
        }
    }

    public void update(SQLiteDatabase db, User user) {
        //实例化内容值
        ContentValues cValue = new ContentValues();
        //在values中添加内容
        cValue.put("_id", user.getId());
        cValue.put("nickname", user.getNickname());
        cValue.put("sex", user.getSex());
        cValue.put("email", user.getEmail());
        cValue.put("password", user.getPassword());
        cValue.put("realName", user.getRealName());
        cValue.put("idCardNumber", user.getIdCardNumber());
        cValue.put("realNameOrNot", user.getRealNameOrNot());
        cValue.put("userImagePath", user.getUserImagePath());
        //修改条件
        String whereClause = "email=?";
        //修改添加参数
        String[] whereArgs = {user.getEmail()};
        //修改
        db.update("user", cValue, whereClause, whereArgs);
    }

    public void updateNickname(SQLiteDatabase db, User user) {
        //实例化内容值
        ContentValues cValue = new ContentValues();
        //在values中添加内容
        cValue.put("nickname", user.getNickname());
        //修改条件
        String whereClause = "email=?";
        //修改添加参数
        String[] whereArgs = {user.getEmail()};
        //修改
        db.update("user", cValue, whereClause, whereArgs);
    }

    public void updateSex(SQLiteDatabase db, User user) {
        //实例化内容值
        ContentValues cValue = new ContentValues();
        //在values中添加内容
        cValue.put("sex", user.getSex());
        //修改条件
        String whereClause = "email=?";
        //修改添加参数
        String[] whereArgs = {user.getEmail()};
        //修改
        db.update("user", cValue, whereClause, whereArgs);
    }
    public void updateUserImagePath(SQLiteDatabase db, String userImagePath) {
        //实例化内容值
        ContentValues cValue = new ContentValues();
        //在values中添加内容
        cValue.put("userImagePath", userImagePath);
        //修改条件
        String whereClause = "email=?";
        //修改添加参数
        String[] whereArgs = {user.getEmail()};
        //修改
        db.update("user", cValue, whereClause, whereArgs);
    }

    /**
     * 判断表是否存在
     *
     * @param db
     * @return
     */
    public boolean isExist(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' and name='user';", null);
        if (cursor.moveToFirst()) {//存在结果集
            cursor.move(0);
            System.out.println(cursor.getString(0));
            return true;
        } else {
            return false;
        }
    }
}
