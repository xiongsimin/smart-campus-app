package com.example.smartcampus.entity;

import java.io.Serializable;

public class ExamInfo implements Serializable {
    private String curriculumName;//课程名
    private String examTime;//考试时间
    private String examPlace;//考试地点
    private String site;//座位号

    public ExamInfo() {
        super();
    }

    public ExamInfo(String curriculumName, String examTime, String examPlace, String site) {
        this.curriculumName = curriculumName;
        this.examTime = examTime;
        this.examPlace = examPlace;
        this.site = site;
    }

    public String getCurriculumName() {
        return curriculumName;
    }

    public void setCurriculumName(String curriculumName) {
        this.curriculumName = curriculumName;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getExamPlace() {
        return examPlace;
    }

    public void setExamPlace(String examPlace) {
        this.examPlace = examPlace;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "ExamInfo{" +
                "curriculumName='" + curriculumName + '\'' +
                ", examTime='" + examTime + '\'' +
                ", examPlace='" + examPlace + '\'' +
                ", site=" + site +
                '}';
    }
}
