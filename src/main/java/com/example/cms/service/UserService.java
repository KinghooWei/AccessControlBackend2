package com.example.cms.service;

import com.example.cms.bean.UserBean;

public interface UserService {
    UserBean loginIn(String name,String password);
}