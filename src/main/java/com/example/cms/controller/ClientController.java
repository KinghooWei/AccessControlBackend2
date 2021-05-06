package com.example.cms.controller;

import com.example.cms.bean.PersonBean;
import com.example.cms.service.PersonService;
import com.example.cms.utils.FaceReg;
import com.example.cms.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Queue;

//import sun.misc.BASE64Decoder;

@RestController
@RequestMapping(value = "/client")
public class ClientController {
    @Autowired
    PersonService personService;

    // -- 通用的json API的接口 -- s//

    /**
     * @param requestBody
     * @return
     */
    @RequestMapping(value = "/api", method = RequestMethod.POST)
    public String handleApi(@RequestBody String requestBody) {
        System.out.println("数据：" + requestBody);
        try {
            JSONObject jsonObject = new JSONObject(requestBody);
            String service = jsonObject.getString("service");
            System.out.println("service：" + service);
            String response = "";
            switch (service) {
                case "client.person.register":      //注册
                    response = responseClientPersonRegister(jsonObject);
                    break;
                case "client.person.login":         //登录
                    response = responseClientPersonLogin(jsonObject);
                    break;
                case "client.person.portraitModify"://头像
                    response = responseClientPersonPortraitModify(jsonObject);
                    break;
                case "client.person.setAddress":    //地址
                    response = responseClientPersonSetAddress(jsonObject);
                    break;
                case "client.person.passwordSet":   //设置密码
                    response = responseClientPasswordSet(jsonObject);
                    break;
                case "client.person.passwordModify"://修改密码
                    response = responseClientPasswordModify(jsonObject);
                    break;
                case "client.person.tempPsd":   //生成临时密码
                    response = responseClientTempPsd(jsonObject);
                    break;
                case "client.person.face":          //人脸
                    response = responseClientFace(jsonObject);
                    break;
                case "client.person.requestQRCode":
                    response = responseClientQRCodeRequest(jsonObject);
                    break;
                case "client.person.destroyQRCode":
                    response = responseClientQRCodeDestroy(jsonObject);
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
            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
        }

    }

    /*
    注册
     */
    private String responseClientPersonRegister(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {
            String name = jsonObject.getString("name");
            String phoneNum = jsonObject.getString("phoneNum");
            String password = jsonObject.getString("loginPassword");

            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                responseJson.put("message", "fail to register, the phone number has already been registered");
                responseJson.put("resultCode", 1);
            } else {
                personService.addPerson(name, phoneNum, password);
                responseJson.put("message", "success to register person");
                responseJson.put("resultCode", -1);
            }
            responseJson.put("timestamp", Utils.getSecondTimestamp());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return responseJson.toString();
    }

    /*
    登录
     */
    private String responseClientPersonLogin(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {
            String phoneNum = jsonObject.getString("phoneNum");
            String password = jsonObject.getString("LoginPassword");
            boolean isAuto = jsonObject.getBoolean("isAuto");

            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                PersonBean person = persons.get(0);
                if (password.equals(person.getLoginPassword())) {
                    if (!isAuto) {
                        //非自动登录
                        String portrait;
                        if (person.getPortrait() != null) {
                            //Base64 Encoded
                            portrait = Base64.getEncoder().encodeToString(person.getPortrait());
                        } else {
                            portrait = null;
                        }
                        responseJson.put("name", person.getName());
                        responseJson.put("portrait", portrait);
                        responseJson.put("community", person.getCommunity());
                        responseJson.put("building", person.getBuilding());
                        responseJson.put("roomId", person.getRoomId());
                        responseJson.put("hasCommunityPwd", !StringUtils.isEmpty(person.getCommunityPassword()));
                        responseJson.put("hasBuildingPwd", !StringUtils.isEmpty(person.getBuildingPassword()));
                        responseJson.put("hasFace", !StringUtils.isEmpty(person.getFaceFeature()));
                    }
                    responseJson.put("message", "success to verify");
                    responseJson.put("resultCode", -1);
                } else {
                    responseJson.put("message", "fail to verify, login password does not match");
                    responseJson.put("resultCode", 0);
                }

            } else {
                responseJson.put("message", "fail to modify, the person does not exist");
                responseJson.put("resultCode", 0);
            }
            responseJson.put("timestamp", Utils.getSecondTimestamp());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return responseJson.toString();
    }

    /*
    设置头像
     */
    private String responseClientPersonPortraitModify(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {

            String phoneNum = jsonObject.getString("phoneNum");
            String faceSrc = jsonObject.getString("portrait");

            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                System.out.println("正在上传");
                System.out.println(faceSrc);
                byte[] faceData = Utils.base64ToBytes(faceSrc); //base64字符串转字节数组
                personService.updatePortrait(phoneNum, faceData);
                responseJson.put("message", "success to modify portrait");
                responseJson.put("resultCode", -1);
            } else {
                responseJson.put("message", "fail to modify, the person does not exist");
                responseJson.put("resultCode", 0);
            }
            responseJson.put("timestamp", Utils.getSecondTimestamp());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return responseJson.toString();
    }

    /*
    设置住址
     */
    private String responseClientPersonSetAddress(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {
            String phoneNum = jsonObject.getString("phoneNum");
            String community = jsonObject.getString("community");
            String building = jsonObject.getString("building");
            String roomId = jsonObject.getString("roomId");

            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                personService.updateCommunity(phoneNum, community);
                personService.updateBuilding(phoneNum, building);
                personService.updateRoomId(phoneNum, roomId);
                responseJson.put("message", "success to set address");
                responseJson.put("resultCode", -1);
            } else {
                responseJson.put("message", "fail to set address, the person does not exist");
                responseJson.put("resultCode", 0);
            }
            responseJson.put("timestamp", Utils.getSecondTimestamp());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJson.toString();
    }

    /*
    设置密码
     */
    private String responseClientPasswordSet(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {

            String phoneNum = jsonObject.getString("phoneNum");
            String password = jsonObject.getString("password");
            String type = jsonObject.getString("passwordType");

            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                switch (type) {
                    case "communitySet":
                        personService.updateCommunityPassword(phoneNum, password);
                        break;
                    case "building":
                        personService.updateBuildingPassword(phoneNum, password);
                        break;
                }
                responseJson.put("message", "success to modify password");
                responseJson.put("resultCode", -1);
            } else {
                responseJson.put("message", "fail to modify password, the person does not exist");
                responseJson.put("resultCode", 0);
            }
            responseJson.put("timestamp", Utils.getSecondTimestamp());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return responseJson.toString();
    }

    /*
    修改密码
     */
    private String responseClientPasswordModify(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {
            String phoneNum = jsonObject.getString("phoneNum");
            String oldPwd = jsonObject.getString("oldPwd");
            String newPwd = jsonObject.getString("newPwd");
            String confPwd = jsonObject.getString("confPwd");
            String type = jsonObject.getString("passwordType");

            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                PersonBean person = persons.get(0);
                switch (type) {
                    case "login":
                        if (oldPwd.equals(person.getLoginPassword())) {     //原密码正确
                            if (newPwd.length() > 3 && newPwd.length() < 21) {  //新密码符合要求
                                if (newPwd.equals(confPwd)) {               //确认密码
                                    personService.updateLoginPassword(phoneNum, newPwd);
                                    responseJson.put("message", "success to modify password");
                                    responseJson.put("resultCode", -1);
                                } else {
                                    responseJson.put("message", "fail to modify password, the passwords entered twice are inconsistent");
                                    responseJson.put("resultCode", 3);
                                }
                            } else {
                                responseJson.put("message", "fail to modify password, the new password does not meet requirements");
                                responseJson.put("resultCode", 2);
                            }
                        } else {
                            responseJson.put("message", "fail to modify password, the original password is incorrect");
                            responseJson.put("resultCode", 1);
                        }
                        break;
                    case "communityModify":
                        if (oldPwd.equals(person.getCommunityPassword())) {     //原密码正确
                            if (newPwd.length() == 6) {                         //密码符合要求
                                if (newPwd.equals(confPwd)) {                   //确认密码
                                    personService.updateCommunityPassword(phoneNum, newPwd);
                                    responseJson.put("message", "success to modify password");
                                    responseJson.put("resultCode", -1);
                                } else {
                                    responseJson.put("message", "fail to modify password, the passwords entered twice are inconsistent");
                                    responseJson.put("resultCode", 3);
                                }
                            } else {
                                responseJson.put("message", "fail to modify password, the new password does not meet requirements");
                                responseJson.put("resultCode", 2);
                            }
                        } else {
                            responseJson.put("message", "fail to modify password, the original password is incorrect");
                            responseJson.put("resultCode", 1);
                        }
                        break;
                    case "buildingModify":
                        if (oldPwd.equals(person.getBuildingPassword())) {      //原密码正确
                            if (newPwd.length() == 6) {
                                if (newPwd.equals(confPwd)) {
                                    personService.updateBuildingPassword(phoneNum, newPwd);
                                    responseJson.put("message", "success to modify password");
                                    responseJson.put("resultCode", -1);
                                } else {
                                    responseJson.put("message", "fail to modify password, the passwords entered twice are inconsistent");
                                    responseJson.put("resultCode", 3);
                                }
                            } else {
                                responseJson.put("message", "fail to modify password, the new password does not meet requirements");
                                responseJson.put("resultCode", 2);
                            }
                        } else {
                            responseJson.put("message", "fail to modify password, the original password is incorrect");
                            responseJson.put("resultCode", 1);
                        }
                        break;
                    default:
                        responseJson.put("message", "fail to modify password, error type: " + type + ". correct type are login、community、building");
                        responseJson.put("resultCode", 0);
                        responseJson.put("timestamp", Utils.getSecondTimestamp());
                        return responseJson.toString();
                }
            } else {
                responseJson.put("message", "fail to modify password, the person does not exist");
                responseJson.put("resultCode", 0);
            }
            responseJson.put("timestamp", Utils.getSecondTimestamp());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return responseJson.toString();
    }

    /*
    临时密码
     */
    private String responseClientTempPsd(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {
            String phoneNum = jsonObject.getString("phoneNum");
            String type = jsonObject.getString("type");

            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                PersonBean person = persons.get(0);
                Utils.TimerManager timerManager;
                String tempPwd = null;
                switch (type) {
                    case "inquire":
                        tempPwd = person.getTempPassword();
                        break;
                    case "open":
                        tempPwd = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));     //生成6位随机临时密码
                        personService.updateTempPassword(phoneNum, tempPwd);                    //更新tempPwd
                        timerManager = Utils.TimerManager.getInstance(phoneNum);                        //定时销毁
                        timerManager.startTimerTask();
                        break;
                    case "close":
                        tempPwd = "";
                        personService.updateTempPassword(phoneNum, tempPwd);
                        timerManager = Utils.TimerManager.getInstance(phoneNum);                        //取消定时销毁
                        timerManager.stopTimerTask();
                        break;
                }
                responseJson.put("tempPwd", tempPwd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJson.toString();
    }

    /*
    人脸信息
     */
    private String responseClientFace(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {
            String phoneNum = jsonObject.getString("phoneNum");
            String faceBase64 = jsonObject.getString("faceBase64");

            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                PersonBean person = persons.get(0);
                byte[] faceBytes = Utils.base64ToBytes(faceBase64); //base64字符串转字节数组

                // 输出人脸到本地
                File file = new File(".\\src\\main\\resources\\facialMask\\faceWithoutMask.jpg");

                OutputStream out = new FileOutputStream(file);
                out.write(faceBytes);
                out.flush();    //输出file到本地，jpg格式，显示正常
                out.close();
                System.out.println("已输出文件");

                // 添加口罩
                Runtime.getRuntime()
                        .exec("C:\\Users\\wuwei\\AppData\\Local\\Programs\\Python\\Python36\\python.exe E:\\IDEAProjects\\demo\\src\\main\\java\\com\\example\\cms\\utils\\facial_mask_for_jython.py");
                // 添加墨镜
                Runtime.getRuntime()
                        .exec("C:\\Users\\wuwei\\AppData\\Local\\Programs\\Python\\Python36\\python.exe E:\\IDEAProjects\\demo\\src\\main\\java\\com\\example\\cms\\utils\\glasses.py");

                // 提取原图特征
                ByteArrayInputStream bais = new ByteArrayInputStream(faceBytes);
                BufferedImage faceBufferedImage = ImageIO.read(bais);   //字节数组输入流转bufferedImage
                FaceReg faceReg = new FaceReg();
                faceReg.initialModel("D:/faceRecognition-jna/weights");
                String faceFeature = faceReg.getFaceFeature(faceBufferedImage);

                // 提取戴口罩的特征
                File fileWithMask = new File("E:\\IDEAProjects\\demo\\src\\main\\resources\\facialMask\\faceWithMask.jpg");
                long time = System.currentTimeMillis();
                while (!fileWithMask.exists() && System.currentTimeMillis() - time < 1000) {
                }
                Thread.sleep(200);
                String featureWithMask = "";
                if (fileWithMask.exists()) {
                    BufferedImage faceWithMaskBI = ImageIO.read(fileWithMask);
                    featureWithMask = faceReg.getFaceFeature(faceWithMaskBI);
                    System.out.println(featureWithMask);
                    fileWithMask.delete();
                }


                // 提取戴墨镜的特征
                File fileWithGlasses = new File("E:\\IDEAProjects\\demo\\src\\main\\resources\\facialMask\\faceWithGlasses.png");
                time = System.currentTimeMillis();
                while (!fileWithGlasses.exists() && System.currentTimeMillis() - time < 1000) {
                }
                Thread.sleep(200);
                String featureWithGlasses = "";
                if (fileWithGlasses.exists()) {
                    BufferedImage faceWithGlassesBI = ImageIO.read(fileWithGlasses);
                    featureWithGlasses = faceReg.getFaceFeature(faceWithGlassesBI);
                    System.out.println(featureWithGlasses);
                    fileWithGlasses.delete();
                }


//                if (!StringUtils.isEmpty(faceFeature)) {
//                    personService.updateFace(phoneNum, faceBytes, faceFeature, featureWithMask, featureWithGlasses);
//                    responseJson.put("resultCode", -1);
//                } else {
//                    responseJson.put("resultCode", 1);
//                }

                if (StringUtils.isEmpty(faceFeature)) {
                    responseJson.put("resultCode", 1);
                    responseJson.put("message", "未检测到人脸");
                } else if (StringUtils.isEmpty(featureWithMask) && StringUtils.isEmpty(featureWithGlasses)) {
                    responseJson.put("resultCode", 2);
                    responseJson.put("message", "生成口罩和墨镜特征失败");
                } else if (StringUtils.isEmpty(featureWithMask)) {
                    responseJson.put("resultCode", 3);
                    responseJson.put("message", "生成口罩特征失败");
                } else if (StringUtils.isEmpty(featureWithGlasses)) {
                    responseJson.put("resultCode", 4);
                    responseJson.put("message", "生成墨镜特征失败");
                } else {
                    responseJson.put("resultCode", -1);
                    responseJson.put("message", "生成人脸特征成功");
                    personService.updateFace(phoneNum, faceBytes, faceFeature, featureWithMask, featureWithGlasses);
                }

            } else {
                responseJson.put("resultCode", 0);
                responseJson.put("message", "账号未注册");
            }
        } catch (JSONException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return responseJson.toString();
    }

    /**
     * 请求二维码
     */
    private String responseClientQRCodeRequest(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {
            String phoneNum = jsonObject.getString("phoneNum");
            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                PersonBean person = persons.get(0);
                String QRCode = String.valueOf((int) ((Math.random() * 9 + 1) * 10000000));
                personService.updateQRCode(phoneNum, QRCode);
                responseJson.put("resultCode", -1);
                responseJson.put("QRCode", QRCode);
                System.out.println(QRCode);
            } else {
                responseJson.put("resultCode", 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJson.toString();
    }

    /**
     * 销毁二维码
     */
    private String responseClientQRCodeDestroy(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        try {
            String phoneNum = jsonObject.getString("phoneNum");
            List<PersonBean> persons = personService.selectPerson(phoneNum);
            if (persons.size() != 0) {
                PersonBean person = persons.get(0);
                personService.updateQRCode(phoneNum, "");
                responseJson.put("resultCode", -1);
            } else {
                responseJson.put("resultCode", 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJson.toString();
    }

    /**
     * 根据byte数组，生成文件
     */
    File getFile(byte[] bfile, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }
}

//    // -- 通用的multipart api 接口 -- s//
//    @PostMapping("/multipartApi") // 等价于 @RequestMapping(value = "/upload", method = RequestMethod.POST)
//    public String handleMultipartApi(HttpServletRequest req, @RequestParam("file") MultipartFile file) {//1. 接受上传的文件  @RequestParam("file") MultipartFile file
//        try {
//            // 获取数据
//            byte[] data;
//            data = file.getBytes();
//            String jsonMessage = req.getParameter("jsonMessage");
//            JSONObject jsonObject = new JSONObject(jsonMessage);
//            String service = jsonObject.getString("service");
//            String response = "";
//            System.out.println(service);
//            switch (service) {
//                case "client.person.add":
//                    String tmpFileName = req.getServletContext().getRealPath("") + "tmp.jpg";
//                    response = responseClientPersonAdd(file, jsonObject, tmpFileName);
//                    break;
//            }
//            return response;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
//        }
//    }
//
//    public String responseClientPersonAdd(MultipartFile file, JSONObject jsonObject, String tmpFileName) {
//        JSONObject responseJson = new JSONObject();
//        try {
//            // 获取数据
//            byte[] data;
//            data = file.getBytes();
//            String personName = jsonObject.getString("personName");
//            String studentNum = jsonObject.getString("studentNum");
//            String password = jsonObject.optString("password", "null");
//            // 将得到的图片数据转成临时文件
//            File tmpFile = new File(tmpFileName);
//            file.transferTo(tmpFile);
//            // 将临时文件读取成BufferedImage，并进行人脸识别与特征提取
//            BufferedImage bufIma = ImageIO.read(tmpFile);
//            tmpFile.delete(); //删除临时文件
//            String feastr = FaceReg.getFaceFeature(bufIma);
//            feastr = "null";
//            System.out.println("feature: " + feastr);
//            if (feastr.isEmpty()) {
//                responseJson.put("timestamp", Utils.getSecondTimestamp());
//                responseJson.put("message", "detect face failed from: " + personName + ", please check the picture file");
//                responseJson.put("resultCode", 0);
//                return responseJson.toString();
//            }
//            // 储存人脸数据进person表中
//            List<PersonBean> persons = personService.selectPerson(studentNum);
//            if (persons.size() != 0) {
////                if(persons.get(0).getName().equals(personName))
////                personService.updatePerson(personName, studentNum, feastr, data, password);
//            } else {
////                personService.insertPerson(personName, studentNum, feastr, data, password);
//            }
//            responseJson.put("timestamp", Utils.getSecondTimestamp());
//            responseJson.put("message", "success to add person: " + personName);
//            responseJson.put("resultCode", -1);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return "{\"resultCode\": -1, \"message\":\"unknown error\"}";
//        }
//        return responseJson.toString();
//    }
//}
