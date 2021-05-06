package com.example.cms.mapper;

import com.example.cms.bean.UserBean;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    UserBean getInfo(@Param("name")String name, @Param("password")String password);
}