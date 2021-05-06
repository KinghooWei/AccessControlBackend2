package com.example.cms.bean;

import java.util.Date;

public class KeyPersonnelBean {
    private String identifyId;

    private String name;

    private byte[] face;

    private String faceFeature;

    private String featureWithMask;

    private String gender;

    private String birth;

    private String type;

    private String caseFile;

    @Override
    public String toString() {
        return "KeyPersonnelBean{" +
                "identifyId='" + identifyId + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birth=" + birth +
                ", type='" + type + '\'' +
                ", caseFile='" + caseFile + '\'' +
                '}';
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFace() {
        return face;
    }

    public void setFace(byte[] face) {
        this.face = face;
    }

    public String getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(String faceFeature) {
        this.faceFeature = faceFeature;
    }

    public String getFeatureWithMask() {
        return featureWithMask;
    }

    public void setFeatureWithMask(String featureWithMask) {
        this.featureWithMask = featureWithMask;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaseFile() {
        return caseFile;
    }

    public void setCaseFile(String caseFile) {
        this.caseFile = caseFile;
    }
}
