package com.example.OCRBackEnd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.OCRBackEnd")
public class OCRBackEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(OCRBackEndApplication.class, args);
    }

}
