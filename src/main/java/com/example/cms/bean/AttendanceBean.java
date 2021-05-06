package com.example.cms.bean;

import java.sql.Date;
import java.sql.Time;

// 考勤记录实体，每个bean对应一条考勤记录
public class AttendanceBean {
    public int person_id;
    public String name;
    public String phoneNum;
    public String enterTime;
    public String community;
    public String building;
    public String method;
    public String longitude;
    public String latitude;
    public byte[] face;
    public String privateKey;
    private byte[] originalFace;

    public int getId() {
        return person_id;
    }

    public void setId(int id) {
        this.person_id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String num) {
        this.phoneNum = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String time) {
        this.enterTime = time;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setFace(byte[] face) {
        this.face = face;
    }

    public byte[] getFace() {
        return face;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getOriginalFace() {
        return originalFace;
    }

    public void setOriginalFace(byte[] originalFace) {
        this.originalFace = originalFace;
    }
}
