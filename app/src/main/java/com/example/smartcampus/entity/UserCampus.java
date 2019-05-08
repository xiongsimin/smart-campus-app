package com.example.smartcampus.entity;


import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 该类对应的表用来维护User表与Campus表之间的关系
 */
public class UserCampus implements Serializable {
    private long id;
    private long userId;
    private int campusId;
    private String attendCampusTime;
    private String campusCardNumber;
    private String campusCardPassword;
    private String academy;
    private String major;
    private String degree;//学历

    public UserCampus() {
        super();
    }

    public UserCampus(long id, long userId, int campusId, String attendCampusTime, String campusCardNumber, String campusCardPassword, String academy, String major, String degree) {
        this.id = id;
        this.userId = userId;
        this.campusId = campusId;
        this.attendCampusTime = attendCampusTime;
        this.campusCardNumber = campusCardNumber;
        this.campusCardPassword = campusCardPassword;
        this.academy = academy;
        this.major = major;
        this.degree = degree;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getCampusId() {
        return campusId;
    }

    public void setCampusId(int campusId) {
        this.campusId = campusId;
    }

    public String getAttendCampusTime() {
        return attendCampusTime;
    }

    public void setAttendCampusTime(String attendCampusTime) {
        this.attendCampusTime = attendCampusTime;
    }

    public String getCampusCardNumber() {
        return campusCardNumber;
    }

    public void setCampusCardNumber(String campusCardNumber) {
        this.campusCardNumber = campusCardNumber;
    }

    public String getCampusCardPassword() {
        return campusCardPassword;
    }

    public void setCampusCardPassword(String campusCardPassword) {
        this.campusCardPassword = campusCardPassword;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        return "UserCampus{" +
                "id=" + id +
                ", userId=" + userId +
                ", campusId=" + campusId +
                ", attendCampusTime=" + attendCampusTime +
                ", campusCardNumber='" + campusCardNumber + '\'' +
                ", campusCardPassword='" + campusCardPassword + '\'' +
                ", academy='" + academy + '\'' +
                ", major='" + major + '\'' +
                ", degree='" + degree + '\'' +
                '}';
    }
}
