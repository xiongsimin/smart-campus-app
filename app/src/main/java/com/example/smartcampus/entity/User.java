package com.example.smartcampus.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {
    private long id;
    private String nickname;
    private String sex;
    private String email;
    private String password;
    private String realName;
    private String idCardNumber;
    private int realNameOrNot;
    private long signUpTime;
    private long userCampusId;
    private String checkCode;
    private long activeTime;
    private String userImagePath;
    private Campus campus;
    private UserCampus userCampus;

    public User() {
        super();
    }

    public User(long id, String nickname, String sex, String email, String password, String realName, String idCardNumber, int realNameOrNot, long signUpTime, long userCampusId, String checkCode, long activeTime, Campus campus, UserCampus userCampus) {
        this.id = id;
        this.nickname = nickname;
        this.sex = sex;
        this.email = email;
        this.password = password;
        this.realName = realName;
        this.idCardNumber = idCardNumber;
        this.realNameOrNot = realNameOrNot;
        this.signUpTime = signUpTime;
        this.userCampusId = userCampusId;
        this.checkCode = checkCode;
        this.activeTime = activeTime;
        this.campus = campus;
        this.userCampus = userCampus;
    }

    public User(long id, String nickname, String sex, String email, String password, String realName, String idCardNumber, int realNameOrNot, long signUpTime, long userCampusId, String checkCode, long activeTime, String userImagePath, Campus campus, UserCampus userCampus) {
        this.id = id;
        this.nickname = nickname;
        this.sex = sex;
        this.email = email;
        this.password = password;
        this.realName = realName;
        this.idCardNumber = idCardNumber;
        this.realNameOrNot = realNameOrNot;
        this.signUpTime = signUpTime;
        this.userCampusId = userCampusId;
        this.checkCode = checkCode;
        this.activeTime = activeTime;
        this.userImagePath = userImagePath;
        this.campus = campus;
        this.userCampus = userCampus;
    }

    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public int getRealNameOrNot() {
        return realNameOrNot;
    }

    public void setRealNameOrNot(int realNameOrNot) {
        this.realNameOrNot = realNameOrNot;
    }

    public long getSignUpTime() {
        return signUpTime;
    }

    public void setSignUpTime(long signUpTime) {
        this.signUpTime = signUpTime;
    }

    public long getUserCampusId() {
        return userCampusId;
    }

    public void setUserCampusId(long userCampusId) {
        this.userCampusId = userCampusId;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public UserCampus getUserCampus() {
        return userCampus;
    }

    public void setUserCampus(UserCampus userCampus) {
        this.userCampus = userCampus;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", realName='" + realName + '\'' +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", realNameOrNot=" + realNameOrNot +
                ", signUpTime=" + signUpTime +
                ", userCampusId=" + userCampusId +
                ", checkCode='" + checkCode + '\'' +
                ", activeTime=" + activeTime +
                ", userImagePath='" + userImagePath + '\'' +
                ", campus=" + campus +
                ", userCampus=" + userCampus +
                '}';
    }
}
