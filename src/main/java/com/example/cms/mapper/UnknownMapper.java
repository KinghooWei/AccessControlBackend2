package com.example.cms.mapper;

import com.example.cms.bean.UnknownBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UnknownMapper {
    List<UnknownBean> selectByPersonId(@Param("personId")String personId);
    List<UnknownBean> getAll();
    void update(UnknownBean unknownBean);
    int insert(UnknownBean unknownBean);
}
