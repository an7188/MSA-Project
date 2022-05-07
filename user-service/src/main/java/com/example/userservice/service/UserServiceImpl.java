package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


//Override Methods -  기능이 구현된 메소드를 변경해서 씀
//Implement Methods - 선언만 된 메소드를 구현
@Service
@Slf4j
public class UserServiceImpl implements UserService{

    BCryptPasswordEncoder passwordEncoder;
    UserRepository userRepository;
    Environment env;
    RestTemplate restTemplate;
    //   @Autowired거보다 생성자를 만들어서 주입하는게 더 좋음
    OrderServiceClient orderServiceClient;

    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, Environment env, RestTemplate restTemplate, OrderServiceClient orderServiceClient) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.env = env;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        // ModelMapper 선언
       ModelMapper mapper = new ModelMapper();
       // ModelMapper가 변환될 수 있는 환경 설정 MatchingStrategies.STRICT -> 딱 맞아 떨지지 않으면 매칭 못하게!
       mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
       //mapper.map(변경 대상 ,변환하고자하는 클래스 이름);
       UserEntity userEntity = mapper.map(userDto,UserEntity.class);

       userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);
        UserDto returnUserDto = mapper.map(userEntity,UserDto.class);
        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null)
            throw new UsernameNotFoundException("User not found");
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//        List<ResponseOrder> orders = new ArrayList<>();
        /* Using as rest template */
//        String orderUrl = String.format(env.getProperty("order_service.url"),userId);
//        //exchange( 주소값, 호출 메소드 타입, 파라미터, 전달 형식)
//        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {
//
//        });
//        List<ResponseOrder> ordersList = orderListResponse.getBody();

        /* Using a feign client */
        /* Feign exception handling */
//        List<ResponseOrder> ordersList = null;
//        try {
//            ordersList = orderServiceClient.getOrders(userId);
//        } catch (FeignException ex) {
//            log.error(ex.getMessage());
//        }
        List<ResponseOrder> ordersList = orderServiceClient.getOrders(userId);

        userDto.setOrders(ordersList);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
         UserEntity userEntity =userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
         UserDto userDto = new ModelMapper().map(userEntity,UserDto.class);
         return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null) // 사용자가 없을경우
            throw new UsernameNotFoundException(username + ": not found");
        //UserDetails은 User로 반환
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());

    }
}
