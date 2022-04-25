package com.example.aipgatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter(){
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
//        return (exchange,chain ) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info(" Global Filter baseMessage: {}", config.getBaseMessage());
//
//            if(config.isPreLogger()){
//                log.info(" Global Filter start: request id-> {}", request.getId());
//            }
//            // Custom Post Filter
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                log.info(" Global Filter End: response code-> {}", response.getStatusCode());
//            }));
//        };
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info(" Logging Filter baseMessage: {}", config.getBaseMessage());

            if(config.isPreLogger()){
                log.info(" Logging PRE Filter : request id-> {}", request.getId());
            }
            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info(" Logging POST Filter : response code-> {}", response.getStatusCode());
            }));
        }, Ordered.LOWEST_PRECEDENCE);
        return filter;
    }
    //Ordered.HIGHEST_PRECEDENCE -> 우선 순위 설정
//exchange -> 우리가 필요한 request와 response를 얻어옴


    // 설정은 application.yml에서 한다.
    @Data
    public static class Config{
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
