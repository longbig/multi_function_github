package com.longbig.multifunction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MultiFunctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiFunctionApplication.class, args);
    }

}
