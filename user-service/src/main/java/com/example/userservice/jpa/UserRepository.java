package com.example.userservice.jpa;

import org.springframework.data.repository.CrudRepository;

// 재너릭 첫번째 타입은 데이터베이스에 연동될 수 있는 엔티티, 기본키에 클래스타입
public interface UserRepository extends CrudRepository<UserEntity,Long> {
    UserEntity findByUserId(String userId);

    UserEntity findByEmail(String username);
}
