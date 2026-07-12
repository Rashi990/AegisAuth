package com.jwt.AegisAuth.repository;

import com.jwt.AegisAuth.entity.RefreshTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshTokenEntity,String> {

    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUsername(String username);
}
