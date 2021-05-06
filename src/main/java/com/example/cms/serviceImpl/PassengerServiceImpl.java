package com.example.cms.serviceImpl;

import com.example.cms.bean.PassengerBean;
import com.example.cms.mapper.PassengerMapper;
import com.example.cms.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService {
    @Autowired
    PassengerMapper passengerMapper;

    @Override
    public List<PassengerBean> getPassengerByFlight(String flight, String date) {
        return passengerMapper.getPassengerByFlight(flight, date);
    }
}
