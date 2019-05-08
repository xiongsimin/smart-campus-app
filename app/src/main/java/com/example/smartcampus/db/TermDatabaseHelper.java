package com.example.smartcampus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.smartcampus.entity.Campus;
import com.example.smartcampus.entity.Term;

import java.util.ArrayList;
import java.util.List;

public class TermDatabaseHelper extends SQLiteOpenHelper {
    private Term term;
    private List<Term> termList;

    public Term getTerm() {
        return term;
    }

    public List<Term> getTermList() {
        return termList;
    }

    public TermDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void create(SQLiteDatabase db) {
        //创建表SQL语句
        String termTable = "create table if not exists term(_id integer primary key, year integer,whichTerm integer,scheduleDataPath text,isPresentTerm integer)";
        //执行SQL语句
        db.execSQL(termTable);
    }

    public void insert(SQLiteDatabase db, Term term) {
        //先将其他学期都设为未选中
        ContentValues cValue0 = new ContentValues();
        cValue0.put("isPresentTerm", 0);
        //修改条件
        String whereClause = "_id>?";
        //修改添加参数
        String[] whereArgs = {String.valueOf(0)};
        db.update("term", cValue0, whereClause, whereArgs);
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //添加学校
        cValue.put("year", term.getYear());
        cValue.put("whichTerm", term.getWhichTerm());
        cValue.put("scheduleDataPath", term.getScheduleDataPath());
        cValue.put("isPresentTerm", 1);
        //调用insert()方法插入数据
        db.insert("term", null, cValue);
    }

    /**
     * 查找所有学期信息
     *
     * @param db
     * @return
     */
    public boolean queryAll(SQLiteDatabase db) {
        termList = new ArrayList<>();
        //查询获得游标
        Cursor cursor = db.query("term", null, null, null, null, null, "year desc");
        //判断游标是否为空
        if (cursor.moveToFirst()) {
            //遍历游标
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.move(i);
                Term term = new Term();
                term.setYear(cursor.getInt(1));
                term.setWhichTerm(cursor.getInt(2));
                term.setScheduleDataPath(cursor.getString(3));
                termList.add(term);
            }
            return true;
        } else {
            System.out.println("本地数据库没有学期信息！");
            return false;
        }
    }

    /**
     * 查询当前学期信息
     */
    public boolean query(SQLiteDatabase db) {
        term = new Term();
        //查询获得游标
        Cursor cursor = db.query("term", null, "isPresentTerm=?", new String[]{String.valueOf(1)}, null, null, null);
        //判断游标是否为空
        if (cursor.moveToFirst()) {
            cursor.move(0);
            term.setYear(cursor.getInt(1));
            term.setWhichTerm(cursor.getInt(2));
            term.setScheduleDataPath(cursor.getString(3));
            return true;
        } else {
            System.out.println("本地数据库没有学期信息！");
            return false;
        }
    }

    /**
     * 查询当前学期信息
     */
    public boolean queryByTerm(SQLiteDatabase db, Term term) {
        this.term = new Term();
        //查询获得游标
        Cursor cursor = db.query("term", null, "year=? and whichTerm=?", new String[]{String.valueOf(term.getYear()), String.valueOf(term.getWhichTerm())}, null, null, null);
        //判断游标是否为空
        if (cursor.moveToFirst()) {
            cursor.move(0);
            this.term.setYear(cursor.getInt(1));
            this.term.setWhichTerm(cursor.getInt(2));
            this.term.setScheduleDataPath(cursor.getString(3));
            return true;
        } else {
            System.out.println("本地数据库没有学期信息！");
            return false;
        }
    }
}
