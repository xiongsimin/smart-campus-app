package com.example.smartcampus.util;

import java.io.Serializable;
import java.util.List;

/**
 * 周课表 一周有7天
 */
public class WeekCurriculumSchedule implements Serializable {
    private int week;//第几周
    /*private DayCurriculumSchedule mondaySchedule;
    private DayCurriculumSchedule tuesdaySchedule;
    private DayCurriculumSchedule wednesdaySchedule;
    private DayCurriculumSchedule thursdaySchedule;
    private DayCurriculumSchedule fridaySchedule;
    private DayCurriculumSchedule saturdaySchedule;
    private DayCurriculumSchedule sundaySchedule;*/

    private List<DayCurriculumSchedule> dayCurriculumScheduleList;

    public WeekCurriculumSchedule() {
        super();
    }

    public WeekCurriculumSchedule(int week, List<DayCurriculumSchedule> dayCurriculumScheduleList) {
        this.week = week;
        this.dayCurriculumScheduleList = dayCurriculumScheduleList;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public List<DayCurriculumSchedule> getDayCurriculumScheduleList() {
        return dayCurriculumScheduleList;
    }

    public void setDayCurriculumScheduleList(List<DayCurriculumSchedule> dayCurriculumScheduleList) {
        this.dayCurriculumScheduleList = dayCurriculumScheduleList;
    }

    @Override
    public String toString() {
        return "WeekCurriculumSchedule{" +
                "week=" + week +
                ", dayCurriculumScheduleList=" + dayCurriculumScheduleList +
                '}';
    }
}
