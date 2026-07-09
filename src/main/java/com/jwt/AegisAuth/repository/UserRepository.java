package com.jwt.AegisAuth.repository;

import com.jwt.AegisAuth.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity,String> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findByRole(String role, Pageable pageable);

    Page<UserEntity> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    Page<UserEntity> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
