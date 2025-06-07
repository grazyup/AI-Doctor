package com.grazy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiDoctorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiDoctorApplication.class, args);
    }

}
