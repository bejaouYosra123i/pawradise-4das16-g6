package com.pawradise.lostandfoundservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lost-and-found")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from lost-and-found-service";
    }
}
