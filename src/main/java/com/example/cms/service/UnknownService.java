package com.example.cms.service;


import com.example.cms.bean.UnknownBean;

import java.util.List;

public interface UnknownService {
    List<UnknownBean> getAllUnknown();

    List<UnknownBean> selectUnknown(String personId);

    void insertUnknown(String enterTime, byte[] portraitData);

}
