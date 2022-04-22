package com.example.secondservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
// Controller와 RestContrller의 차이점은 주어진 response body를 직접 구현하느냐 주어진거 쓰느냐
@RequestMapping("/second-service")
public class SecondServiceController {
    @GetMapping("/welcome")
    public String welcome(){
        return "welcome to the Second service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request") String header){

        log.info(header);
        return "Hello Word int Second Service";
    }
    @GetMapping("/check")
    public String check() {
        return "Hi, there. This is a message from Second Service ";
    }
}
