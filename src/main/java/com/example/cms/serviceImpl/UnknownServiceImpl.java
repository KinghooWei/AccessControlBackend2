package com.example.cms.serviceImpl;

import com.example.cms.bean.UnknownBean;
import com.example.cms.mapper.UnknownMapper;
import com.example.cms.service.UnknownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnknownServiceImpl implements UnknownService {
    @Autowired
    private UnknownMapper unknownMapper;

    @Override
    public List<UnknownBean> getAllUnknown(){
        return unknownMapper.getAll();
    }

    @Override
    public List<UnknownBean> selectUnknown(String personId){
        return unknownMapper.selectByPersonId(personId);
    }

    @Override
    public void insertUnknown(String enterTime, byte[] portraitData){
        UnknownBean unknownBean = new UnknownBean();
        unknownBean.setEnterTime(enterTime);
        unknownBean.setPortrait(portraitData);
        unknownMapper.insert(unknownBean);
    }
}
