package com.example.SuperMarketApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example")
@EntityScan(basePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example")
public class SuperMarketAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperMarketAppApplication.class, args);
        System.out.println("Super market application started");
    }
}
