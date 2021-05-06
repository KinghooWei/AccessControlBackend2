package com.example.cms.controller;

import com.example.cms.bean.Account;
import com.example.cms.bean.UserBean;
import com.example.cms.service.AccountService;
import com.example.cms.utils.JWTUtils;
import com.example.cms.utils.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;


    @RequestMapping(value = "/api/hello")
    public String Hello(@RequestHeader("Authorization") String token) {
        System.out.println(token+"Authoirizdfaijoj token de zhi");
        return "xwlab后台加密数据";
    }

    @RequestMapping(value = "/accountLogin",method = RequestMethod.POST)
    public Results login(@RequestBody Account account) {

        Account accountName = accountService.loginAccount(account);

        if(accountName != null) {
            String jwt = JWTUtils.generateToken(account.getUserName());
            return new Results(1, "ok", jwt);
        }

        return new Results(0,"error", "账户或密码错误");

    }



    @RequestMapping(value = "/api/insertAccount", method = RequestMethod.POST)
    public Results InsertAccount(@RequestBody Account account) {
        int account1 =accountService.InsertAccount(account);
        System.out.println(account1);
        return new Results(1,"success");
    }

    @RequestMapping(value = "/api/deleteAccount/{id}", method = RequestMethod.DELETE)
    public Results deleteAccount(@PathVariable int id) {
        int del =  accountService.DeleteAccount(id);
        return new Results(1, "success");
    }

    @RequestMapping(value = "/api/updateAccount/{id}", method = RequestMethod.POST)
    public Results updateAccount(@RequestBody Account account, @PathVariable int id) {

        System.out.println(id);
        System.out.println(account);
        System.out.println("--------------------------------------");

        int update = accountService.updateAccount(account, id);
        return new Results(1, "success");
    }
}
