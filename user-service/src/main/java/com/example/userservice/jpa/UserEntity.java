package com.example.userservice.jpa;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "users")
public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 생성하고 Identity타입으로 생성
    private Long id;

    @Column(nullable = false,length = 50,unique = true)
    private String email;
    @Column(nullable = false,length = 50)
    private String name;
    @Column(nullable = false,unique = true)
    private String userId;
    @Column(nullable = false,unique = true)
    private String encryptedPwd;
}
