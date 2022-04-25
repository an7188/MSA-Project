package com.example.userservice.controller;

import com.example.userservice.vo.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

//    @Autowired application.yml에 파일을 이용할 수 있는 방법 1
    private Environment env;

    @Autowired
    private Greeting greeting;

    public UserController(Environment env) {
        this.env = env;
    }

    @GetMapping("/health_check")
    public String status(){
        return "It's Working in User service";
    }

    @GetMapping("/welcome")
    public String welcome(){
//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

}
