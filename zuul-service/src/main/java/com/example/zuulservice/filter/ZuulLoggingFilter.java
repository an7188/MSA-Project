package com.example.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

//빈 생성
@Component
@Slf4j
public class ZuulLoggingFilter extends ZuulFilter {

//    Logger logger = LoggerFactory.getLogger(ZuulLoggingFilter.class);
    //실제 어떤 동작을 하는지
    @Override
    public Object run() throws ZuulException {
        log.info("**************** printing logs : ");
        //request 정보를 가지고 있는 최상의 -> ResquestContext
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("**************** " + request.getRequestURI());
        return null;
    }
    //사전 필터인지 사후 필터인지
    @Override
    public String filterType() {
        return "pre";
    }

    // 여러개의 필더가 있을경우 순서
    @Override
    public int filterOrder() {
        return 1;
    }

    //옵션에 따라서 필터를 쓸건지
    @Override
    public boolean shouldFilter() {
        return true;
    }



}
