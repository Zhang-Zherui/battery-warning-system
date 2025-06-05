package com.xiaomi.servicesignal;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableDubbo
public class ServiceSignalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSignalApplication.class, args);
    }

}
