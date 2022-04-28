package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


//Override Methods -  기능이 구현된 메소드를 변경해서 씀
//Implement Methods - 선언만 된 메소드를 구현
@Service
public class UserServiceImpl implements UserService{

    BCryptPasswordEncoder passwordEncoder;
    UserRepository userRepository;

    //   @Autowired거보다 생성자를 만들어서 주입하는게 더 좋음
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
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
