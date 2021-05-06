package com.example.cms.service;


import com.example.cms.bean.AttendanceBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttendanceService {
    List<AttendanceBean> getAllAttendance();

    List<AttendanceBean> selectByGateNum(String gateNum, String lastQueryTime);

    void insertAttendance(String name, String phoneNum, String community, String building, String method, String longitude, String latitude, byte[] face, String privateKey);

    void insertAttendance(String name, String phoneNum, String community, String building, String method, String longitude, String latitude);

    byte[] selectFaceByEnterTime(String enterTime);

    List<AttendanceBean> selectAttendanceByEnterTime(String enterTime);

    List<AttendanceBean> selectAttendanceByName(String name);
}