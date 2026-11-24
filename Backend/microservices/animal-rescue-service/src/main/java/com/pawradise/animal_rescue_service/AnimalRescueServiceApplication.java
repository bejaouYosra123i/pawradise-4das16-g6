package com.pawradise.animal_rescue_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AnimalRescueServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimalRescueServiceApplication.class, args);
    }

}