package com.example.cms.controller;
import com.example.cms.bean.AttendanceBean;
import com.example.cms.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.sql.Timestamp;

import java.util.*;

@Controller
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;
    //    @RequestMapping(value = "/api/important")
//    @ResponseBody
//    public String Hello(@RequestHeader("Authorization") String token) {
//        System.out.println(token+"Authoirizdfaijoj token de zhi");
//        List<Path> paths=pathService.getAllPath();
//        int len=paths.size();
//        StringBuffer res=new StringBuffer();
//        for(int i=0;i<len;i++){
//            res.append(paths.get(i).timeId);
//            res.append(paths.get(i).name);
//            res.append(paths.get(i).place);
//            res.append(paths.get(i).submissionDate);
//        }
//        return res.toString();
//    }
//    @RequestMapping(value = " ")
//    //    @ResponseBody
//    public String test(Model model){
////        ModelAndView mv = new ModelAndView();
////        mv.setViewName("person_table_new");
//        List<AttendanceBean> attendanceBeans = attendanceService.getAllAttendance();
//        System.out.println("person-table");
////        System.out.println(persons.toString());
////        System.out.println(persons.get(0).getName());
//        model.addAttribute("paths", paths);
//        return "person-table";
//    }
//    @RequestMapping(value = "/test1")
////    @ResponseBody
//    public String test1(Model model){
////        ModelAndView mv = new ModelAndView();
////        mv.setViewName("person_table_new");
//        List<Path> paths = pathService.getAllPath();
//        model.addAttribute("paths", paths.toString());
//        return "valuetest";
//    }
//    @RequestMapping(value = "/test3")
//    @ResponseBody
//    public String test1(){
////        ModelAndView mv = new ModelAndView();
////        mv.setViewName("person_table_new");
//        List<Path> paths = pathService.getAllPath();
////        model.addAttribute("paths", paths.toString());
//        return paths.toString();
//    }
//    @RequestMapping(value = "/test2")
//    public String test2(Model model){
////        ModelAndView mv = new ModelAndView();
////        mv.setViewName("person_table_new");
//        List<Path> paths = pathService.getAllPath();
//        System.out.println("person-table");
////        System.out.println(persons.toString());
////        System.out.println(persons.get(0).getName());
//        model.addAttribute("paths", paths);
//        return "newtest";
//    }
    @RequestMapping(value = "/qureyname")
    public String qurey2(String qurey2,Model model){
        System.out.println(qurey2);
        String name=qurey2;
        List<AttendanceBean> attendances = attendanceService.selectAttendanceByName(name);
        System.out.println("person-table");
//        System.out.println(persons.toString());
//        System.out.println(persons.get(0).getName());
        model.addAttribute("attendances", attendances);
        System.out.println(attendances.toString());
        return "attendance-table";
    }

    @RequestMapping(value = "/qureyway")
    public String qurey3(@RequestParam("qurey3")String qurey3, Model model){
        System.out.println(qurey3);
        List<AttendanceBean> attendances = attendanceService.selectAttendanceByName(qurey3);
        int len=attendances.size();
        model.addAttribute("name", qurey3);
        model.addAttribute("firsttime", attendances.get(0).getEnterTime());
        model.addAttribute("lasttime", attendances.get(len-1).getEnterTime());
        String[] position=new String[2*len];
        for(int i=0;i<len;i++){
            position[i*2]=attendances.get(i).getLongitude();
            position[i*2+1]=attendances.get(i).getLatitude();
        }
        model.addAttribute("position", position);
//        String start=attendances.get(0).getEnterTime();
//        String end=attendances.get(len-1).getEnterTime();
//        String[] a=start.split(" ");
//        String[] b=end.split(" ");
//        String[] aa=a[0].split("-");
//        String[] bb=b[0].split("-");
//        int[] nyr=new int[3];
//        nyr[0]=Integer.parseInt(bb[0])-Integer.parseInt(aa[0]);
//        nyr[1]=Integer.parseInt(bb[1])-Integer.parseInt(aa[1]);
//        nyr[2]=Integer.parseInt(bb[2])-Integer.parseInt(aa[2]);
//        int days=nyr[0]*365+nyr[1]*30+nyr[2];
//        String[] aaa=a[1].split(":");
//        String[] bba=b[1].split(":");
//        int[] sfm=new int[3];
        return "newtest";
    }
//    @RequestMapping(value = "/test2")
//    public String test2(Model model){
////        ModelAndView mv = new ModelAndView();
////        mv.setViewName("person_table_new");
//        List<Path> paths = pathService.getAllPath();
//        System.out.println("person-table");
////        System.out.println(persons.toString());
////        System.out.println(persons.get(0).getName());
//        model.addAttribute("paths", paths);
//        return "newtest";
//    }
//    @RequestMapping(value = "/qureyway")
//    public String qurey3(String qurey3,Model model){
//        List<Path> paths = pathService.seach(qurey3);
//        System.out.println("person-table");
////        System.out.println(persons.toString());
////        System.out.println(persons.get(0).getName());
//        model.addAttribute("paths", paths);
//        return "person-table";
//    }
//    @RequestMapping(value = "/testvalue")
//    @ResponseBody
//    public String qurey3(){
//        Double[][] paths=new Double[][]{{113.353704,23.159208},{113.353722,23.159329},{113.354688,23.15937},{113.354759,23.158768},{113.354243,23.158731},{113.354229,23.158722}};
//        String result="";
//        for(int i=0;i<6;i++){
//            result=result+paths[i][0].toString()+',';
//            if(i==5) result=result+paths[i][1].toString();
//            else result=result+paths[i][1].toString()+',';
//        }
//        return result;
//    }
}
