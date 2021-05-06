package com.example.cms.serviceImpl;

import com.example.cms.bean.AttendanceBean;
import com.example.cms.mapper.AttendanceMapper;
import com.example.cms.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    //将DAO注入Service层
    @Autowired
    private AttendanceMapper attendanceMapper;

    @Override
    public List<AttendanceBean> getAllAttendance() {
        return attendanceMapper.getAll();
    }

    @Override
    public List<AttendanceBean> selectByGateNum(String gateNum, String lastQueryTime) {
        return attendanceMapper.selectByGateNum(gateNum, lastQueryTime);
    }

    @Override
    public void insertAttendance(String name, String phoneNum, String community, String building, String method, String longitude, String latitude, byte[] face, String privateKey) {
        AttendanceBean attendanceBean = new AttendanceBean();
        attendanceBean.setName(name);
        attendanceBean.setPhoneNum(phoneNum);
        attendanceBean.setCommunity(community);
        attendanceBean.setBuilding(building);
        attendanceBean.setMethod(method);
        attendanceBean.setLongitude(longitude);
        attendanceBean.setLatitude(latitude);
        attendanceBean.setFace(face);
        attendanceBean.setPrivateKey(privateKey);
        attendanceMapper.insert(attendanceBean);
    }

    @Override
    public void insertAttendance(String name, String phoneNum, String community, String building, String method, String longitude, String latitude) {
        insertAttendance(name, phoneNum, community, building, method, longitude, latitude, null, null);
    }

    @Override
    public byte[] selectFaceByEnterTime(String enterTime) {
        return attendanceMapper.selectFaceByEnterTime(enterTime);
    }

    @Override
    public List<AttendanceBean> selectAttendanceByEnterTime(String enterTime) {
        return attendanceMapper.selectAttendanceByEnterTime(enterTime);
    }

    @Override
    public List<AttendanceBean> selectAttendanceByName(String name) {
        return attendanceMapper.selectAttendanceByName(name);
    }
}
