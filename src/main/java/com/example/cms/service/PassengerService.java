package com.example.cms.service;

import com.example.cms.bean.PassengerBean;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;


public interface PassengerService {

    List<PassengerBean> getPassengerByFlight(String flight, String date);
}
