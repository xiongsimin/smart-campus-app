package com.example.smartcampus.util;

import java.io.Serializable;
import java.util.List;

/**
 * 学期
 */
public class SemesterSchedule implements Serializable {
    private String year;//学年 如：2017-2018学年
    private int term;//学期 如：1
    private List<WeekCurriculumSchedule> weekCurriculumScheduleList;

    public SemesterSchedule() {
        super();
    }

    public SemesterSchedule(String year, int term, List<WeekCurriculumSchedule> weekCurriculumScheduleList) {
        this.year = year;
        this.term = term;
        this.weekCurriculumScheduleList = weekCurriculumScheduleList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public List<WeekCurriculumSchedule> getWeekCurriculumScheduleList() {
        return weekCurriculumScheduleList;
    }

    public void setWeekCurriculumScheduleList(List<WeekCurriculumSchedule> weekCurriculumScheduleList) {
        this.weekCurriculumScheduleList = weekCurriculumScheduleList;
    }

    @Override
    public String toString() {
        return "SemesterSchedule{" +
                "year='" + year + '\'' +
                ", term=" + term +
                ", weekCurriculumScheduleList=" + weekCurriculumScheduleList +
                '}';
    }
}
