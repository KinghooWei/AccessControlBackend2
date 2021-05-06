package com.example.cms.controller;

import com.example.cms.bean.UserBean;
import com.example.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    //将Service注入Web层
    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public String show(){
        return "login";
    }

    @RequestMapping(value = "/loginIn",method = RequestMethod.POST)
    public String login(String name,String password){
        UserBean userBean = userService.loginIn(name,password);
        if(userBean!=null){
            return "main";
        }else {
            return "error";
        }
    }

    @RequestMapping(value = "/menu")
    public String menu(){
        return "menu";
    }

    @RequestMapping(value = "/profile")
    public String profile(){
        System.out.println("profile");
        return "profile";
    }

    @RequestMapping(value = "/fontawesome")
    public String fontawesome(){
        System.out.println("fontawesome");
        return "fontawesome";
    }

    @RequestMapping(value = "/main")
    public String main(){
        System.out.println("main");
        return "main";
    }

}