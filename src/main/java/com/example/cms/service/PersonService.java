package com.example.cms.service;


import com.example.cms.bean.PersonBean;

import java.util.List;

public interface PersonService {
    List<PersonBean> getAllPerson();            //获取所有人员信息

    List<PersonBean> selectPerson(String phoneNum);     //根据手机号获取人员

    List<PersonBean> getPersonByAddressAndTime(String community, String gate, String lastUpdateTime);     //根据楼门和上次跟新时间获取人员

    List<PersonBean> getPersonByCommunityAndTime(String community, String lastUpdateTime);     //根据小区和上次跟新时间获取人员

    void addPerson(String name, String phoneNum, String loginPassword);

    void updateLoginPassword(String phoneNum, String password);

    void updateCommunity(String phoneNum, String community);

    void updateBuilding(String phoneNum, String building);

    void updateRoomId(String phoneNum, String roomId);

    void updateCommunityPassword(String phoneNum, String communityPassword);

    void updateBuildingPassword(String phoneNum, String buildingPassword);

    void updateTempPassword(String phoneNum, String tempPassword);

    void updateQRCode(String phoneNum, String QRCode);

    void updatePortrait(String phoneNum, byte[] portrait);

    void updateFaceFeature(String phoneNum, String faceFeature);

    void updateFaceImg(String phoneNum, byte[] faceImg);

    void updateFace(String phoneNum, byte[] faceImg, String faceFeature, String featureWithMask, String featureWithGlasses);
}
