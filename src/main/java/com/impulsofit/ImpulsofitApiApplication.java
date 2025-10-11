package com.impulsofit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.impulsofit")
public class ImpulsofitApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImpulsofitApiApplication.class, args);
    }
}