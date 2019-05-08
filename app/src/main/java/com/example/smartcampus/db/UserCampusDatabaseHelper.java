package com.example.smartcampus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.smartcampus.entity.Campus;
import com.example.smartcampus.entity.User;
import com.example.smartcampus.entity.UserCampus;

public class UserCampusDatabaseHelper extends SQLiteOpenHelper {
    private UserCampus userCampus;

    public UserCampusDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        userCampus = new UserCampus();
    }

    public UserCampus getUserCampus() {
        return userCampus;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void create(SQLiteDatabase db) {
        //创建表SQL语句
        String campusTable = "create table if not exists user_campus(_id integer primary key, userId integer,campusId integer,attendCampusTime text,campusCardNumber text,campusCardPassword text,academy text,major text,degree text)";
        //执行SQL语句
        db.execSQL(campusTable);
    }
    public void insert(SQLiteDatabase db, UserCampus userCampus) {
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //添加
        cValue.put("_id", userCampus.getId());
        cValue.put("userId", userCampus.getUserId());
        cValue.put("campusId", userCampus.getCampusId());
        cValue.put("attendCampusTime", userCampus.getAttendCampusTime());
        cValue.put("campusCardNumber", userCampus.getCampusCardNumber());
        cValue.put("campusCardPassword", userCampus.getCampusCardPassword());
        cValue.put("academy", userCampus.getAcademy());
        cValue.put("major", userCampus.getMajor());
        cValue.put("degree", userCampus.getDegree());
        //调用insert()方法插入数据
        db.insert("user_campus", null, cValue);
    }

    public boolean query(SQLiteDatabase db) {
        userCampus = new UserCampus();
        //查询获得游标
        Cursor cursor = db.query("user_campus", null, null, null, null, null, null);
        //判断游标是否为空
        if (cursor.moveToFirst()) {
            cursor.move(0);
            userCampus.setId(cursor.getInt(0));
            userCampus.setUserId(cursor.getInt(1));
            userCampus.setCampusId(cursor.getInt(2));
            userCampus.setAttendCampusTime(cursor.getString(3));
            userCampus.setCampusCardNumber(cursor.getString(4));
            userCampus.setCampusCardPassword(cursor.getString(5));
            userCampus.setAcademy(cursor.getString(6));
            userCampus.setMajor(cursor.getString(7));
            userCampus.setDegree(cursor.getString(8));
            return true;
        } else {
            System.out.println("本地数据库没有学生-学校信息！");
            return false;
        }
    }

    /**
     * 更新本地用户-学校关系表
     * @param db
     * @param userCampus
     */
    public void update(SQLiteDatabase db, UserCampus userCampus) {
        //实例化内容值
        ContentValues cValue = new ContentValues();
        //在values中添加内容
        cValue.put("_id", userCampus.getId());
        cValue.put("userId", userCampus.getUserId());
        cValue.put("campusId", userCampus.getCampusId());
        cValue.put("attendCampusTime", userCampus.getAttendCampusTime());
        cValue.put("campusCardNumber", userCampus.getCampusCardNumber());
        cValue.put("campusCardPassword", userCampus.getCampusCardPassword());
        cValue.put("academy", userCampus.getAcademy());
        cValue.put("major", userCampus.getMajor());
        cValue.put("degree", userCampus.getDegree());
        //修改条件
        String whereClause = "_id>?";
        //修改添加参数
        String[] whereArgs = {String.valueOf(0)};
        //修改
        db.update("user_campus", cValue, whereClause, whereArgs);
    }

    /**
     * 更新本地数据库学院信息
     * @param db
     * @param userCampus
     */
    public void updateAcademy(SQLiteDatabase db, UserCampus userCampus) {
        //实例化内容值
        ContentValues cValue = new ContentValues();
        //在values中添加内容
        cValue.put("academy", userCampus.getAcademy());
        //修改条件
        String whereClause = "_id>?";
        //修改添加参数
        String[] whereArgs = {String.valueOf(0)};
        //修改
        db.update("user_campus", cValue, whereClause, whereArgs);
    }
    /**
     * 更新本地数据库专业信息
     * @param db
     * @param userCampus
     */
    public void updateMajor(SQLiteDatabase db, UserCampus userCampus) {
        //实例化内容值
        ContentValues cValue = new ContentValues();
        //在values中添加内容
        cValue.put("major", userCampus.getMajor());
        //修改条件
        String whereClause = "_id>?";
        //修改添加参数
        String[] whereArgs = {String.valueOf(0)};
        //修改
        db.update("user_campus", cValue, whereClause, whereArgs);
    }
    /**
     * 更新本地数据库专业信息
     * @param db
     * @param userCampus
     */
    public void updateDegree(SQLiteDatabase db, UserCampus userCampus) {
        //实例化内容值
        ContentValues cValue = new ContentValues();
        //在values中添加内容
        cValue.put("degree", userCampus.getDegree());
        //修改条件
        String whereClause = "_id>?";
        //修改添加参数
        String[] whereArgs = {String.valueOf(0)};
        //修改
        db.update("user_campus", cValue, whereClause, whereArgs);
    }
    /**
     * 更新本地数据库入学时间信息
     * @param db
     * @param userCampus
     */
    public void updateAttendCampusTime(SQLiteDatabase db, UserCampus userCampus) {
        //实例化内容值
        ContentValues cValue = new ContentValues();
        //在values中添加内容
        cValue.put("attendCampusTime", userCampus.getAttendCampusTime());
        //修改条件
        String whereClause = "_id>?";
        //修改添加参数
        String[] whereArgs = {String.valueOf(0)};
        //修改
        db.update("user_campus", cValue, whereClause, whereArgs);
    }

    /**
     * 判断表是否存在
     * @param db
     * @return
     */
    public boolean isExist(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' and name='user_campus';", null);
        if(cursor.moveToFirst()){//存在结果集
            cursor.move(0);
            System.out.println(cursor.getString(0));
            return true;
        }else {
            return false;
        }
    }
}
