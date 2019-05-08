package com.example.smartcampus.entity;

import java.io.Serializable;

public class Campus implements Serializable {
    private int id;
    private String campusName;
    private String campusCode;
    private String province;
    private int educationSystemId;
    private int officialSiteId;
    private String educationSystemAddress;

    public Campus() {
        super();
    }

    public Campus(int id, String campusName, String campusCode, String province, int educationSystemId, int officialSiteId, String educationSystemAddress) {
        this.id = id;
        this.campusName = campusName;
        this.campusCode = campusCode;
        this.province = province;
        this.educationSystemId = educationSystemId;
        this.officialSiteId = officialSiteId;
        this.educationSystemAddress = educationSystemAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public void setCampusCode(String campusCode) {
        this.campusCode = campusCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getEducationSystemId() {
        return educationSystemId;
    }

    public void setEducationSystemId(int educationSystemId) {
        this.educationSystemId = educationSystemId;
    }

    public int getOfficialSiteId() {
        return officialSiteId;
    }

    public void setOfficialSiteId(int officialSiteId) {
        this.officialSiteId = officialSiteId;
    }

    public String getEducationSystemAddress() {
        return educationSystemAddress;
    }

    public void setEducationSystemAddress(String educationSystemAddress) {
        this.educationSystemAddress = educationSystemAddress;
    }

    @Override
    public String toString() {
        return "Campus{" +
                "id=" + id +
                ", campusName='" + campusName + '\'' +
                ", campusCode='" + campusCode + '\'' +
                ", province='" + province + '\'' +
                ", educationSystemId=" + educationSystemId +
                ", officialSiteId=" + officialSiteId +
                ", educationSystemAddress='" + educationSystemAddress + '\'' +
                '}';
    }
}
