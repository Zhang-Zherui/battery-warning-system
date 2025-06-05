package com.xiaomi.servicerule;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ServiceRuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRuleApplication.class, args);
    }

}
