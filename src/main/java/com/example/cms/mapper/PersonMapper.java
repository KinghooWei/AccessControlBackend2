package com.example.cms.mapper;

import com.example.cms.bean.PersonBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonMapper {
    PersonBean getInfo(@Param("name") String name);

    List<PersonBean> selectByPhoneNum(@Param("phoneNum") String studentNum);

    List<PersonBean> getAll();

    List<PersonBean> getPersonByAddressAndTime(@Param("community") String community, @Param("gate") String gate, @Param("lastUpdateTime") String lastUpdateTime);

    List<PersonBean> getPersonByCommunityAndTime(@Param("community") String community, @Param("lastUpdateTime") String lastUpdateTime);

    int insert(PersonBean personBean);

    void updateLoginPassword(@Param("phoneNum") String phoneNum, @Param("loginPassword") String loginPassword);

    void updateCommunity(@Param("phoneNum") String phoneNum, @Param("community") String community);

    void updateBuilding(@Param("phoneNum") String phoneNum, @Param("building") String building);

    void updateRoomId(@Param("phoneNum") String phoneNum, @Param("roomId") String roomId);

    void updateCommunityPassword(@Param("phoneNum") String phoneNum, @Param("communityPassword") String communityPassword);

    void updateBuildingPassword(@Param("phoneNum") String phoneNum, @Param("buildingPassword") String buildingPassword);

    void updateTempPassword(@Param("phoneNum") String phoneNum, @Param("tempPassword") String tempPassword);

    void updatePortrait(@Param("phoneNum") String phoneNum, @Param("portrait") byte[] portrait);

    void updateFaceFeature(@Param("phoneNum") String phoneNum, @Param("faceFeature") String faceFeature);

    void updateFaceImg(@Param("phoneNum") String phoneNum, @Param("faceImg") byte[] faceImg);

    void updateFace(@Param("phoneNum") String phoneNum, @Param("faceImg") byte[] faceImg, @Param("faceFeature") String faceFeature, @Param("featureWithMask") String featureWithMask, @Param("featureWithGlasses") String featureWithGlasses);

    void updateQRCode(@Param("phoneNum") String phoneNum, @Param("QRCode") String QRCode);
}
