package com.example.firstservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;

@Slf4j
@RestController
// Controller와 RestContrller의 차이점은 주어진 response body를 직접 구현하느냐 주어진거 쓰느냐
@RequestMapping("/first-service")
public class FirstServiceController {

    Environment env;

    @Autowired
    public FirstServiceController(Environment env){
        this.env=env;
    }
    @GetMapping("/welcome")
    public String welcome(){
        return "welcome to the First service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header){

        log.info(header);
        return "Hello Word int First Service";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        // 로드 밸런서 -> 포트 번호 확인
        log.info("server port={}",request.getServerPort());
        return String.format( "Hi, there. This is a message from First Service on port %s",env.getProperty("local.server.port"));
    }
}
