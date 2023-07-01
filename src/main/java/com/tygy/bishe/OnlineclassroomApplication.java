package com.tygy.bishe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.tygy.bishe.mapper")
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class OnlineclassroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineclassroomApplication.class, args);
    }

}
