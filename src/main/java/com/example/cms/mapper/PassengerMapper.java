package com.example.cms.mapper;

import com.example.cms.bean.PassengerBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PassengerMapper {

    // 根据航班和日期加载乘客信息
    List<PassengerBean> getPassengerByFlight(@Param("flight") String flight, @Param("date") String date);
}
