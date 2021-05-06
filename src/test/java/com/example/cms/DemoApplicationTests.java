package com.example.cms;

import com.example.cms.bean.UserBean;
import com.example.cms.service.UserService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.example.demo.mapper")
class DemoApplicationTests {
    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        UserBean userBean = userService.loginIn("wegen","123456");
        System.out.println("该用户ID为： ");
        System.out.println(userBean.getId());
        System.out.println("该用户name为： ");
        System.out.println(userBean.getName());
        System.out.println("该用户password为： ");
        System.out.println(userBean.getPassword());
    }

}
