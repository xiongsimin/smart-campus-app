package com.example.smartcampus.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 日课表 包括6节大课
 */
public class DayCurriculumSchedule implements Serializable {
    private int day;//周几
   /* private List<Curriculum> firstCurriculum;
    private List<Curriculum> secondCurriculum;
    private List<Curriculum> thirdCurriculum;
    private List<Curriculum> fourthCurriculum;
    private List<Curriculum> fifthCurriculum;
    private List<Curriculum> sixthCurriculum;*/
    private List<Curriculum> curriculumList;

    public DayCurriculumSchedule() {
        super();
        /*curriculumList=new ArrayList<>();
        for(int i=0;i<6;i++){
            curriculumList.add(new Curriculum());
        }*/
    }

    public DayCurriculumSchedule(int day, List<Curriculum> curriculumList) {
        this.day = day;
        this.curriculumList = curriculumList;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<Curriculum> getCurriculumList() {
        return curriculumList;
    }

    public void setCurriculumList(List<Curriculum> curriculumList) {
        this.curriculumList = curriculumList;
    }

    @Override
    public String toString() {
        return "DayCurriculumSchedule{" +
                "day=" + day +
                ", curriculumList=" + curriculumList +
                '}';
    }
}
