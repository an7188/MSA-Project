package com.example.aipgatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AipgatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AipgatewayServiceApplication.class, args);
    }
    @Bean
    public HttpTraceRepository httpTraceRepository(){
        // 클라이언트에서 요청했던 트레이스 정보가 메모에 담겨서 우리가 필요할때 엔드포인트로 담겨 제공
     return new InMemoryHttpTraceRepository();
    }
}
