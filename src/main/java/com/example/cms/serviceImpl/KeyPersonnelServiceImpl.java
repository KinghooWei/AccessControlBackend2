package com.example.cms.serviceImpl;

import com.example.cms.bean.KeyPersonnelBean;
import com.example.cms.mapper.KeyPersonnelMapper;
import com.example.cms.service.KeyPersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyPersonnelServiceImpl implements KeyPersonnelService {
    @Autowired
    KeyPersonnelMapper keyPersonnelMapper;

    @Override
    public List<KeyPersonnelBean> getAllKeyPersonnel() {
        return keyPersonnelMapper.getAllKeyPersonnel();
    }
}
