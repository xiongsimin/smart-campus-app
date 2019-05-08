package com.example.smartcampus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.smartcampus.entity.Campus;
import com.example.smartcampus.entity.User;

public class CampusDatabaseHelper extends SQLiteOpenHelper {
    private Campus campus;

    public CampusDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        campus = new Campus();
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    /*    private int id;
            private String campusName;
            private String campusCode;
            private String province;
            private int educationSystemId;
            private int officialSiteId;
            private String educationSystemAddress;*/
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void create(SQLiteDatabase db) {
        //创建表SQL语句
        String campusTable = "create table if not exists campus(_id integer primary key, campusName text,campusCode text,province text,educationSystemId integer,officialSiteId integer,educationSystemAddress text)";
        //执行SQL语句
        db.execSQL(campusTable);
    }
    public void insert(SQLiteDatabase db, Campus campus) {
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //添加学校
        cValue.put("_id", campus.getId());
        cValue.put("campusName", campus.getCampusName());
        cValue.put("campusCode", campus.getCampusCode());
        cValue.put("province", campus.getProvince());
        cValue.put("educationSystemId", campus.getEducationSystemId());
        cValue.put("officialSiteId", campus.getOfficialSiteId());
        cValue.put("educationSystemAddress", campus.getEducationSystemAddress());
        //调用insert()方法插入数据
        db.insert("campus", null, cValue);
    }

    public boolean query(SQLiteDatabase db) {
        campus = new Campus();
        //查询获得游标
        Cursor cursor = db.query("campus", null, null, null, null, null, null);
        //判断游标是否为空
        if (cursor.moveToFirst()) {
            cursor.move(0);
            campus.setId(cursor.getInt(0));
            campus.setCampusName(cursor.getString(1));
            campus.setCampusCode(cursor.getString(2));
            campus.setProvince(cursor.getString(3));
            campus.setEducationSystemId(cursor.getInt(4));
            campus.setOfficialSiteId(cursor.getInt(5));
            campus.setEducationSystemAddress(cursor.getString(6));
            return true;
        } else {
            System.out.println("本地数据库没有学校信息！");
            return false;
        }
    }
    public void update(SQLiteDatabase db, Campus campus) {
        //实例化内容值
        ContentValues cValue = new ContentValues();
        //在values中添加内容
        cValue.put("_id", campus.getId());
        cValue.put("campusName", campus.getCampusName());
        cValue.put("campusCode", campus.getCampusCode());
        cValue.put("province", campus.getProvince());
        cValue.put("educationSystemId", campus.getEducationSystemId());
        cValue.put("officialSiteId", campus.getOfficialSiteId());
        cValue.put("educationSystemAddress", campus.getEducationSystemAddress());
        //修改条件
        String whereClause = "_id>?";
        //修改添加参数
        String[] whereArgs = {String.valueOf(0)};
        //修改
        db.update("campus", cValue, whereClause, whereArgs);
    }
    /**
     * 判断表是否存在
     * @param db
     * @return
     */
    public boolean isExist(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' and name='campus';", null);
        if(cursor.moveToFirst()){//存在结果集
            cursor.move(0);
            System.out.println(cursor.getString(0));
            return true;
        }else {
            return false;
        }
    }
}
