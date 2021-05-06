package com.example.cms.controller;

import com.example.cms.bean.PersonBean;
import com.example.cms.service.AttendanceService;
import com.example.cms.service.PersonService;
import com.example.cms.service.UnknownService;
import com.example.cms.utils.FaceReg;
import com.example.cms.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
/*
    DoorController 实现的是后台与门禁机的接口部分，通过post请求完成两者的信息交互
 */

@RestController
@RequestMapping(value = "/door")
public class DoorController {

    @Autowired
    AttendanceService attendanceService;
    @Autowired
    PersonService personService;
    @Autowired
    UnknownService unknownService;

    // -- 通用的json API的接口 -- s//
    @RequestMapping(value = "/api", method = RequestMethod.POST)
    public String handleApi(@RequestBody String requestBody) {
        System.out.println("数据：" + requestBody);
        try {
            JSONObject jsonObject = new JSONObject(requestBody);
            String service = jsonObject.getString("service");
            String response = "";
            switch (service) {
                case "door.attendance.enter":
                    response = responseDoorAttendanceEnter(jsonObject); //添加用户出入信息
                    break;
                case "door.person.getAll":
                    response = responseDoorPersonGetAll(jsonObject);    //加载用户信息至门禁机
                    break;
                case "door.attendance.QRCode":
                    response = responseDoorAttendanceQRCode(jsonObject);    //验证二维码
                    break;
                case "heartbeat.getUsers":
                    response = responseHeartbeatGetUsers(jsonObject);    //心率设备加载人员信息
                    break;
                default:
//                    System.out.println("未处理的服务请求："+service);
                    response = "{\"resultCode\": -2, \"message\":\"unknown service\"}";
                    break;
            }
            System.out.println(response);
            return response;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return "{\"resultCode\": 0, \"message\":\"unknown error\"}";
        }

    }

    /**
     * 二维码验证
     */
    String responseDoorAttendanceQRCode(JSONObject jsonObject) throws JSONException {
        JSONObject responseJson = new JSONObject();
        String phoneNum = jsonObject.optString("phoneNum");
        String QRCode = jsonObject.optString("QRCode");
        List<PersonBean> persons = personService.selectPerson(phoneNum);
        if (persons.size() != 0) {
            PersonBean person = persons.get(0);
            if (QRCode.equals(person.getQRCode())) {
                String name = person.getName();
                responseJson.put("resultCode", -1);
                responseJson.put("personName", name);
                String community = jsonObject.getString("community");
                String building = jsonObject.getString("building");
                String longitude = jsonObject.getString("longitude");
                String latitude = jsonObject.getString("latitude");
//            String enterTimeConvert = enterTime.substring(0, 4) + "-" + enterTime.substring(4, 6) + "-" + enterTime.substring(6, 8) + " " + enterTime.substring(8, 10) + ":" + enterTime.substring(10, 12);
                attendanceService.insertAttendance(name, phoneNum, community, building, "QRCode", longitude, latitude);
            } else {
                responseJson.put("resultCode", 0);
            }
        }
        return responseJson.toString();
    }

//    void addAttendance(JSONObject jsonObject, String method) {
//        JSONObject responseJson = new JSONObject();
//        try {
//            String name = jsonObject.getString("name");
//            String studentNum = jsonObject.getString("phoneNum");
//            String community = jsonObject.getString("community");
//            String building = jsonObject.getString("building");
////            String enterTimeConvert = enterTime.substring(0, 4) + "-" + enterTime.substring(4, 6) + "-" + enterTime.substring(6, 8) + " " + enterTime.substring(8, 10) + ":" + enterTime.substring(10, 12);
//            attendanceService.insertAttendance(name, studentNum, community, building, method);
//            responseJson.put("timestamp", Utils.getSecondTimestamp());
//            responseJson.put("message", "success to add enter message");
//            responseJson.put("resultCode", 0);
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//    }

    String responseDoorPersonGetAll(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        List<PersonBean> persons;
        try {
//            String timestamp = jsonObject.getString("timestamp");
            String community = jsonObject.getString("community");
            String gate = jsonObject.getString("gate");
            String lastUpdateTime = jsonObject.getString("lastUpdateTime");
            if (gate.endsWith("门")) {
                persons = personService.getPersonByCommunityAndTime(community, lastUpdateTime);
            } else {
                persons = personService.getPersonByAddressAndTime(community, gate, lastUpdateTime);
            }
            JSONArray userArray = new JSONArray();
            for (PersonBean user : persons) {
                System.out.println(user.toString());
                JSONObject userJson = new JSONObject();
                userJson.put("name", user.getName());
                userJson.put("phoneNum", user.getPhoneNum());
                userJson.put("password", user.getBuildingPassword());
                userJson.put("feature", user.getFaceFeature());
                userJson.put("featureWithMask", user.getFeatureWithMask());
                userJson.put("featureWithGlasses", user.getFeatureWithGlasses());
                userArray.put(userJson);
            }
            responseJson.put("userArray", userArray);
            responseJson.put("timestamp", Utils.getSecondTimestamp());
            responseJson.put("resultCode", -1);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return responseJson.toString();
    }

    /*
    心率设备加载人员信息
     */
    String responseHeartbeatGetUsers(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        List<PersonBean> userList;
        try {
            userList = personService.getAllPerson();
            JSONArray userInfos = new JSONArray();
            for (PersonBean user : userList) {
                System.out.println(user.toString());
                JSONObject userInfo = new JSONObject();
                userInfo.put("name", user.getName());
                userInfo.put("phoneNum", user.getPhoneNum());
                userInfo.put("feature", user.getFaceFeature());
                userInfo.put("featureWithMask", user.getFeatureWithMask());
                userInfos.put(userInfo);
            }
            responseJson.put("userList", userInfos);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        System.out.println(responseJson.toString());
        return responseJson.toString();
    }

    /*
   添加门禁记录
     */
    String responseDoorAttendanceEnter(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {
            String name = jsonObject.getString("name");
            String phoneNum = jsonObject.getString("phoneNum");
            String faceBase64 = jsonObject.getString("faceBase64");
            double key = jsonObject.getDouble("key");
            byte[] face = Utils.base64ToBytes(faceBase64);
            String community = jsonObject.getString("community");
            String building = jsonObject.getString("building");
//            String method = jsonObject.getString("method");
            String longitude = jsonObject.getString("longitude");
            String latitude = jsonObject.getString("latitude");
//            String enterTimeConvert = enterTime.substring(0, 4) + "-" + enterTime.substring(4, 6) + "-" + enterTime.substring(6, 8) + " " + enterTime.substring(8, 10) + ":" + enterTime.substring(10, 12);
            attendanceService.insertAttendance(name, phoneNum, community, building, "人脸识别", longitude, latitude, face, Double.toString(key));
//            responseJson.put("timestamp", Utils.getSecondTimestamp());
            responseJson.put("message", "success to add enter message");
            responseJson.put("resultCode", -1);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return responseJson.toString();
    }
    // -- 通用的json API的接口的接口 -- e//

    // 测试图片文件上传接口（/personAdd）
    @PostMapping("/upload") // 等价于 @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String getUploadFile(HttpServletRequest req, @RequestParam("file") MultipartFile file, Model m) {//1. 接受上传的文件  @RequestParam("file") MultipartFile file
        try {
            String fileName = System.currentTimeMillis() + file.getOriginalFilename();
            String personName = req.getParameter("personName");
            System.out.println(personName);
            String destFileName = req.getServletContext().getRealPath("") + "uploaded" + File.separator + fileName;
            System.out.println(destFileName);
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            file.transferTo(destFile);
            m.addAttribute("fileName", fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "{\"resultCode\": -1, \"message\":\"failed to upload\"}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"resultCode\": -1, \"message\":\"failed to upload\"}";
        }

        return "{\"resultCode\": 0, \"message\":\"success to upload\"}";
    }


    // -- 通用的multipart api 接口 -- s//
    @PostMapping("/multipartApi") // 等价于 @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleMultipartApi(HttpServletRequest req, @RequestParam("file") MultipartFile file) {//1. 接受上传的文件  @RequestParam("file") MultipartFile file
        try {
            // 获取数据
            byte[] data;
            data = file.getBytes();
            String jsonMessage = req.getParameter("jsonMessage");
            JSONObject jsonObject = new JSONObject(jsonMessage);
            String service = jsonObject.getString("service");
            String response = "";
            System.out.println(service);
            switch (service) {
                case "door.unknown.report":
                    response = responseDoorUnknownReport(file, jsonObject);
                    break;
                case "door.person.add":
                    String tmpFileName = req.getServletContext().getRealPath("") + "tmp.jpg";
                    response = responseDoorPersonAdd(file, jsonObject, tmpFileName);
                    break;
            }
            return response;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
        } catch (JSONException e) {
            e.printStackTrace();
            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
        }
    }

    String responseDoorUnknownReport(MultipartFile file, JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {
            // 获取数据
            byte[] data;
            data = file.getBytes();
            String enterTime = jsonObject.getString("enterTime");
            // 储存人脸数据进unknown表中
            unknownService.insertUnknown(enterTime, data);
            responseJson.put("timestamp", Utils.getSecondTimestamp());
            responseJson.put("message", "success to add unknown in " + enterTime);
            responseJson.put("resultCode", 0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
        } catch (JSONException e) {
            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
        }
        return responseJson.toString();
    }

    public String responseDoorPersonAdd(MultipartFile file, JSONObject jsonObject, String tmpFileName) {
        JSONObject responseJson = new JSONObject();
        try {
            // 获取数据
            byte[] data;
            data = file.getBytes();
            String personName = jsonObject.getString("personName");
            String phoneNum = jsonObject.getString("phoneNum");
            String password = jsonObject.optString("LoginPassword", "null");
            // 将得到的图片数据转成临时文件
            File tmpFile = new File(tmpFileName);
            file.transferTo(tmpFile);
            // 将临时文件读取成BufferedImage，并进行人脸识别与特征提取
            BufferedImage bufIma = ImageIO.read(tmpFile);
            tmpFile.delete(); //删除临时文件
            String feastr = FaceReg.getFaceFeature(bufIma);
            System.out.println("feature: " + feastr);
            if (feastr.isEmpty()) {
                responseJson.put("timestamp", Utils.getSecondTimestamp());
                responseJson.put("message", "detect face failed from: " + personName + ", please check the picture file");
                responseJson.put("resultCode", -1);
                return responseJson.toString();
            }
            // 储存人脸数据进person表中
            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
//                if(persons.get(0).getName().equals(personName))
//                personService.updateLoginPassword(phoneNum, password);
            } else {
//                personService.addPerson(personName, phoneNum,  password);
            }
            responseJson.put("timestamp", Utils.getSecondTimestamp());
            responseJson.put("message", "success to add person: " + personName);
            responseJson.put("resultCode", 0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
        } catch (JSONException e) {
            e.printStackTrace();
            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
        }
        return responseJson.toString();
    }

    // -- 通用的multipart api 接口 -- e//

}
