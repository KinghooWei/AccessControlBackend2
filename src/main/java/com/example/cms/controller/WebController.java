package com.example.cms.controller;

import com.example.cms.bean.Account;
import com.example.cms.bean.AttendanceBean;
import com.example.cms.bean.PersonBean;
import com.example.cms.bean.UnknownBean;
import com.example.cms.service.AccountService;
import com.example.cms.service.AttendanceService;
import com.example.cms.service.PersonService;
import com.example.cms.service.UnknownService;
import com.example.cms.utils.Utils;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static com.example.cms.utils.Utils.*;

@Controller
public class WebController {
    //将Service注入Web层
    @Autowired
    PersonService personService;
    @Autowired
    AccountService accountService;

    @RequestMapping("/person-table")
    public String showPersonTable(Model model){
        List<PersonBean> persons = personService.getAllPerson();
        System.out.println("person-table");
//        System.out.println(persons.toString());
//        System.out.println(persons.get(0).getName());
        model.addAttribute("persons", persons);
        return "person-table";
    }

    @RequestMapping("/getPersonImg.do")
    public void getPersonImg(HttpServletRequest request, HttpServletResponse response){
        try {
//            System.out.println("getPersonImg");
            String phoneNum = request.getParameter("phoneNum");
            PersonBean person = personService.selectPerson(phoneNum).get(0);
//            System.out.println(person.getName());
            byte[] byteAry = person.getFaceImg();
            if (byteAry != null) {
                response.setContentType("image/png");
                ServletOutputStream out = response.getOutputStream();
                out.write(byteAry);
                out.flush();
                out.close();
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/getUnknownImg.do")
    public void getUnknowImg(HttpServletRequest request, HttpServletResponse response){
        try {
            System.out.println("getUnknownImg");
            String personId = request.getParameter("personId");
            UnknownBean unknownBean = unknownService.selectUnknown(personId).get(0);
            System.out.println(unknownBean.getEnterTime());
            byte[] byteAry =  unknownBean.getPortrait();
            System.out.println(byteAry);
            response.setContentType("image/png");
            ServletOutputStream out = response.getOutputStream();
            out.write(byteAry);
            out.flush();
            out.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Autowired
    AttendanceService attendanceService;

    @RequestMapping("/attendance-table")
    public String showAttendanceTable(Model model){
        List<AttendanceBean> attendances = attendanceService.getAllAttendance();
        System.out.println("attendance-table");
        System.out.println(attendances.toString());
//        System.out.println(attendances.get(0).getName());
        model.addAttribute("attendances", attendances);
        return "attendance-table";
    }

    @RequestMapping("/getAttendanceImg.do")
    public void getAttendanceImg(HttpServletRequest request, HttpServletResponse response){
        try {
//            System.out.println("getAttendanceImg");
            String enterTime = request.getParameter("enterTime");
            AttendanceBean attendance = attendanceService.selectAttendanceByEnterTime(enterTime).get(0);
//            System.out.println(person.getName());
            byte[] byteAry = attendance.getFace();
            if (byteAry != null) {
                response.setContentType("image/png");
                ServletOutputStream out = response.getOutputStream();
                out.write(byteAry);
                out.flush();
                out.close();
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/access-record-decrypt")
    public String showAccessRecordDecrypt(Model model){
        List<AttendanceBean> attendances = attendanceService.getAllAttendance();
//        System.out.println("attendance-table");
//        System.out.println(attendances.toString());
//        System.out.println(attendances.get(0).getName());
        model.addAttribute("attendances", attendances);
        return "access-record-decrypt";
    }

    @RequestMapping("/getDecryptImg.do")
    public void getDecryptImg(HttpServletRequest request, HttpServletResponse response){
        try {
//            System.out.println("解码");
            String enterTime = request.getParameter("enterTime");
            AttendanceBean attendance = attendanceService.selectAttendanceByEnterTime(enterTime).get(0);
            String method = attendance.getMethod();
            if (method.equals("人脸识别")) {
                byte[] byteAry = attendance.getFace();
                double key = Double.parseDouble(attendance.getPrivateKey());
                ByteArrayInputStream in = new ByteArrayInputStream(byteAry);
                BufferedImage bImg = ImageIO.read(in);
                int[][] pixels = getPixels(bImg);
                BufferedImage bImgDecrypt = processBitmap(pixels,key);
                byte[] byteOut = buffered2bytes(bImgDecrypt);
                response.setContentType("image/png");
                ServletOutputStream out = response.getOutputStream();
                out.write(byteOut);
                out.flush();
                out.close();
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/access-record-decrypt",method = RequestMethod.POST)
    public String login(String name, String password,Model model){
        Account account = accountService.loginAccount(new Account(name,password));
        if(account!=null){
            List<AttendanceBean> attendances = attendanceService.getAllAttendance();
            model.addAttribute("attendances", attendances);
            return "access-record-decrypt";
        }else {
            return "error";
        }
    }

    @Autowired
    UnknownService unknownService;
    @RequestMapping("/unknown-table")
    public String showUnknownTable(Model model){
        List<UnknownBean> unknownBeans = unknownService.getAllUnknown();
        System.out.println("unknown-table");
        System.out.println(unknownBeans.toString());
        model.addAttribute("unknowns", unknownBeans);
        return "unknown-table";
    }
}
