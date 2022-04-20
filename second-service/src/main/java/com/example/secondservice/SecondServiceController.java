package com.example.secondservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// Controller와 RestContrller의 차이점은 주어진 response body를 직접 구현하느냐 주어진거 쓰느냐
@RequestMapping("/")
public class SecondServiceController {
    @GetMapping("welcome")
    public String welcome(){
        return "welcome to the Second service";
    }
}
