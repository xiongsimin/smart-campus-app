package com.example.smartcampus.entity;

import java.io.Serializable;

public class Term implements Serializable {
    private int year;//学年 记录起始学年 如2015-2016学年，此处记录2015,
    private int whichTerm;//第几学期
    private String scheduleDataPath;//课表数据存储文件路径（文件名）

    public Term() {
        super();
    }

    public Term(int year, int whichTerm) {
        this.year = year;
        this.whichTerm = whichTerm;
    }

    public Term(int year, int whichTerm, String scheduleDataPath) {
        this.year = year;
        this.whichTerm = whichTerm;
        this.scheduleDataPath = scheduleDataPath;
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWhichTerm() {
        return whichTerm;
    }

    public void setWhichTerm(int whichTerm) {
        this.whichTerm = whichTerm;
    }

    public String getScheduleDataPath() {
        return scheduleDataPath;
    }

    public void setScheduleDataPath(String scheduleDataPath) {
        this.scheduleDataPath = scheduleDataPath;
    }



    @Override
    public String toString() {
        return "Term{" +
                "year=" + year +
                ", whichTerm=" + whichTerm +
                ", scheduleDataPath='" + scheduleDataPath + '\'' +
                '}';
    }
}
