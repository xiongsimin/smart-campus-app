package com.example.smartcampus.util;

import android.support.annotation.ColorRes;

import com.example.smartcampus.R;

import java.io.Serializable;

public class Curriculum implements Serializable {
    private String curriculumName;//课程名称
    private String type;//课程类型
    private String classRoom;//上课教室
    private int startWeek;//起始周
    private int endWeek;//结束周
    private int startTime;//开始时间（第几节课） 按大课算 每天6节
//    private int endTime;//结束时间（第几节课）
    private String teacher;//授课老师
    private int color;//该课程背景色

    public Curriculum() {
        super();
        color= R.color.white;
    }

    public Curriculum(String curriculumName, String type, String classRoom, int startWeek, int endWeek, int startTime, String teacher, int color) {
        this.curriculumName = curriculumName;
        this.type = type;
        this.classRoom = classRoom;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.startTime = startTime;
        this.teacher = teacher;
        this.color = color;
    }

    public String getCurriculumName() {
        return curriculumName;
    }

    public void setCurriculumName(String curriculumName) {
        this.curriculumName = curriculumName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Curriculum{" +
                "curriculumName='" + curriculumName + '\'' +
                ", type='" + type + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", startTime=" + startTime +
                ", teacher='" + teacher + '\'' +
                ", color=" + color +
                '}';
    }
}
