package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId); // 한명의 데이터 검색
    Iterable<UserEntity> getUserByAll(); // Iterable반복적인 데이터

    UserDto getUserDetailsByEmail(String userName);
}
