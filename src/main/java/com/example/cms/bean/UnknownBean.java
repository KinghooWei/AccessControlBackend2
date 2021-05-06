package com.example.cms.bean;

public class UnknownBean {
    public int person_id;
    public String enter_time;
    public byte[] portrait;
    public int getId() {
        return person_id;
    }

    public void setId(int id) {
        this.person_id = id;
    }

    public String getEnterTime() {
        return enter_time;
    }

    public void setEnterTime(String enterTime) {
        this.enter_time = enterTime;
    }

    public void setPortrait(byte[] portraitData) { this.portrait = portraitData; }

    public byte[] getPortrait() { return portrait; }
}
