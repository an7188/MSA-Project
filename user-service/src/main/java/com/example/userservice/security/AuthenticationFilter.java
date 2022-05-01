package com.example.userservice.security;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UserService userService;
    private Environment env;


    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment env) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    // 입력 받은 아이디와 비밀 번호를 받아 인증 정보 만듬
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 질문!
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
// 매니저가 만들어진 토큰으로 인증 처리
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(// 토큰형태로 만들어줌
                            creds.getEmail(), // 이메일과
                            creds.getPassword(), //패스워드를 넘기면
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(userName);
        //userName이 아닌 UserId로 토큰 발급 할 수 있도록!

        // 토큰 생성
        String token = Jwts.builder()
                .setSubject(userDetails.getUserId()) // 토큰 만듬
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time")))) // 토큰 시간 설정 (현재시간+ application.yml에 있는 시간)
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret")) // 암호화 해줌
                .compact();

        //만들어진 토큰을 헤더에 넣기 , 토큰이 정상적으로 만들어진건지 확인하기 위해 userId도 넣어줌
        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());

    }
}
