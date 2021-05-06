package com.example.cms.controller;

import com.example.cms.bean.PassengerBean;
import com.example.cms.service.PassengerService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("passenger")
public class PassengerController {

    @Autowired
    PassengerService passengerService;

    @RequestMapping(value = "/api", method = RequestMethod.POST)
    public String handleApi(@RequestBody String requestBody) {
        System.out.println("数据：" + requestBody);
        try {
            JSONObject jsonObject = new JSONObject(requestBody);
            String service = jsonObject.getString("service");
            String response = "";
            switch (service) {
                case "passenger.getListByFlight":
                    response = getPassengerList(jsonObject); //加载乘客
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

    String getPassengerList(JSONObject jsonObject) {
        JSONObject responseJson = new JSONObject();
        List<PassengerBean> passengers;
        try {
            String flight = jsonObject.getString("flight");
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            passengers = passengerService.getPassengerByFlight(flight, df.format(date));
            JSONArray passengerInfos = new JSONArray();
            for (PassengerBean passenger : passengers) {
                System.out.println(passenger.toString());
                JSONObject passengerInfo = new JSONObject();
                passengerInfo.put("name", passenger.getPassengerName());
                passengerInfo.put("identifyId", passenger.getPassengerId());
                passengerInfo.put("class", passenger.getFlightClass());
                passengerInfo.put("seat", passenger.getSeat());
                passengerInfo.put("gate", passenger.getGate());
                passengerInfo.put("faceBase64", Base64.getEncoder().encodeToString(passenger.getPassengerFace()));
                passengerInfo.put("feature", passenger.getFaceFeature());
                passengerInfo.put("featureWithMask", passenger.getFeatureWithMask());
                passengerInfos.put(passengerInfo);
            }
            responseJson.put("from", passengers.get(0).getFrom());
            responseJson.put("to", passengers.get(0).getTo());
            responseJson.put("passengerList", passengerInfos);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        System.out.println(responseJson.toString());
        return responseJson.toString();
    }

    private JSONArray featureString2Array(String featureString) {
        String[] stringArray = featureString.trim().split(" ");
        JSONArray featureArray = new JSONArray();
        for (String s : stringArray) {
            featureArray.put(s);
        }
        return featureArray;
    }
}
