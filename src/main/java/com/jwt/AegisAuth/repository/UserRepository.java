package com.jwt.AegisAuth.repository;

import com.jwt.AegisAuth.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity,String> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByUsernameContainingIgnoreCase(String username);
    List<UserEntity> findByEmailContainingIgnoreCase(String email);

}
