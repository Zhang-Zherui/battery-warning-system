package com.xiaomi.servicewarning;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDubbo
@SpringBootApplication
@EnableScheduling
public class ServiceWarningApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceWarningApplication.class, args);
    }

}
