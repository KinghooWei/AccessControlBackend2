package com.example.cms.mapper;

import com.example.cms.bean.AttendanceBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttendanceMapper {
    List<AttendanceBean> getAll();

    List<AttendanceBean> selectByGateNum(@Param("gateNum") String gateNum, @Param("lastQueryTime") String lastQueryTime);

    int insert(AttendanceBean attendanceBean);

    byte[] selectFaceByEnterTime(@Param("enterTime") String enterTime);

    List<AttendanceBean> selectAttendanceByEnterTime(@Param("enterTime") String enterTime);

    List<AttendanceBean> selectAttendanceByName(@Param("name") String name);
}
