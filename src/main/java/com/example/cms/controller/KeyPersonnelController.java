package com.example.cms.controller;

import com.example.cms.bean.KeyPersonnelBean;
import com.example.cms.service.KeyPersonnelService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("keyPersonnel")
public class KeyPersonnelController {
    @Autowired
    KeyPersonnelService keyPersonnelService;

    @RequestMapping(value = "/api", method = RequestMethod.POST)
    public String handleApi(@RequestBody String requestBody) {
        System.out.println("数据：" + requestBody);
        try {
            JSONObject jsonObject = new JSONObject(requestBody);
            String service = jsonObject.getString("service");
            String response = "";
            switch (service) {
                case "keyPersonnel.getList":
                    response = getKeyPersonList(jsonObject);    // 获取重点人员信息
                    break;
                default:
                    System.out.println("未处理的服务请求：" + service);
                    response = "{\"resultCode\": -2, \"message\":\"unknown service\"}";
                    break;
            }
            return response;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return "{\"resultCode\": 0, \"message\":\"unknown error\"}";
        }

    }

    String getKeyPersonList(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        List<KeyPersonnelBean> keyPersonnelList;
        try {
            keyPersonnelList = keyPersonnelService.getAllKeyPersonnel();
            JSONArray keyPersonnelInfos = new JSONArray();
            for (KeyPersonnelBean keyPersonnel : keyPersonnelList) {
                System.out.println(keyPersonnel.toString());
                JSONObject keyPersonnelInfo = new JSONObject();
                keyPersonnelInfo.put("name", keyPersonnel.getName());
                keyPersonnelInfo.put("identifyId", keyPersonnel.getIdentifyId());
                keyPersonnelInfo.put("gender", keyPersonnel.getGender());
                keyPersonnelInfo.put("birth", keyPersonnel.getBirth());
                keyPersonnelInfo.put("type", keyPersonnel.getType());
                keyPersonnelInfo.put("caseFile", keyPersonnel.getCaseFile());
                keyPersonnelInfo.put("faceBase64", Base64.getEncoder().encodeToString(keyPersonnel.getFace()));
                keyPersonnelInfo.put("feature", keyPersonnel.getFaceFeature());
                keyPersonnelInfo.put("featureWithMask", keyPersonnel.getFeatureWithMask());
                keyPersonnelInfos.put(keyPersonnelInfo);
            }
            responseJson.put("keyPersonnelList", keyPersonnelInfos);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        System.out.println(responseJson.toString());
        return responseJson.toString();
    }
}
