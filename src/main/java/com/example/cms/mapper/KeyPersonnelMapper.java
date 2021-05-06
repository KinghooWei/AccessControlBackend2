package com.example.cms.mapper;

import com.example.cms.bean.KeyPersonnelBean;

import java.util.List;

public interface KeyPersonnelMapper {

    // 获取所有重点人员
    List<KeyPersonnelBean> getAllKeyPersonnel();
}
