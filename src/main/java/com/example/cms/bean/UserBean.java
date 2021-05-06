package com.example.cms.bean;

//用户信息实体，每个bean对应一个用户（也就是管理人员）
public class UserBean {
    private int user_id;
    private String user_name;
    private String password;

    public int getId() {
        return user_id;
    }

    public void setId(int id) {
        this.user_id = id;
    }

    public String getName() {
        return user_name;
    }

    public void setName(String name) {
        this.user_name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}