package com.example.cms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.demo.mapper")
public class DemoApplication {
    public static void main(String[] args) {
//        FaceReg.initialModel("e:/jna/build/weights");
        SpringApplication.run(DemoApplication.class, args);
    }

}
