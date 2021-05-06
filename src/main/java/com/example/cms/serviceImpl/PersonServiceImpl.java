package com.example.cms.serviceImpl;

import com.example.cms.bean.PersonBean;
import com.example.cms.mapper.PersonMapper;
import com.example.cms.service.PersonService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@MapperScan("com.example.cms.mapper")
@Service
public class PersonServiceImpl implements PersonService {
    //将DAO注入Service层

    @Autowired
    private PersonMapper personMapper;

    @Override
    public List<PersonBean> getAllPerson() {
        return personMapper.getAll();
    }

    @Override
    public List<PersonBean> getPersonByAddressAndTime(String community, String gate, String lastUpdateTime) {
        return personMapper.getPersonByAddressAndTime(community, gate, lastUpdateTime);
    }

    @Override
    public List<PersonBean> getPersonByCommunityAndTime(String community, String lastUpdateTime) {
        return personMapper.getPersonByCommunityAndTime(community, lastUpdateTime);
    }

    @Override
    public void addPerson(String name, String phoneNum, String loginPassword) {
        PersonBean personBean = new PersonBean();
        personBean.setName(name);
        personBean.setPhoneNum(phoneNum);
        personBean.setLoginPassword(loginPassword);
        personMapper.insert(personBean);
    }


    @Override
    public List<PersonBean> selectPerson(String phoneNum) {
        return personMapper.selectByPhoneNum(phoneNum);
    }

    @Override
    public void updateLoginPassword(String phoneNum, String password) {
        personMapper.updateLoginPassword(phoneNum, password);
    }

    @Override
    public void updateCommunity(String studentNum, String password) {
        personMapper.updateCommunity(studentNum, password);
    }

    @Override
    public void updateBuilding(String phoneNum, String building) {
        personMapper.updateBuilding(phoneNum, building);
    }

    @Override
    public void updateRoomId(String phoneNum, String roomId) {
        personMapper.updateRoomId(phoneNum, roomId);
    }

    @Override
    public void updateCommunityPassword(String phoneNum, String communityPassword) {
        personMapper.updateCommunityPassword(phoneNum, communityPassword);
    }

    @Override
    public void updateBuildingPassword(String phoneNum, String buildingPassword) {
        personMapper.updateBuildingPassword(phoneNum, buildingPassword);
    }

    @Override
    public void updateTempPassword(String phoneNum, String tempPassword) {
        personMapper.updateTempPassword(phoneNum, tempPassword);
    }

    @Override
    public void updatePortrait(String phoneNum, byte[] portrait) {
        personMapper.updatePortrait(phoneNum, portrait);
    }

    @Override
    public void updateFaceFeature(String phoneNum, String faceFeature) {
        personMapper.updateFaceFeature(phoneNum, faceFeature);
    }

    @Override
    public void updateFaceImg(String phoneNum, byte[] faceImg) {
        personMapper.updateFaceImg(phoneNum, faceImg);
    }

    @Override
    public void updateFace(String phoneNum, byte[] faceImg, String faceFeature, String featureWithMask, String featureWithGlasses) {
        personMapper.updateFace(phoneNum, faceImg, faceFeature, featureWithMask, featureWithGlasses);
    }

    @Override
    public void updateQRCode(String phoneNum, String QRCode) {
        personMapper.updateQRCode(phoneNum, QRCode);
    }
}
