package com.yupi.usercenterbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@MapperScan("com.yupi.usercenterbackend.mapper")
@EnableScheduling
public class UserCenterBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterBackendApplication.class, args);
    }

}
