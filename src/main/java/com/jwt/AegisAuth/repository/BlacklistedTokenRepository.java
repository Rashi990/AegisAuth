package com.jwt.AegisAuth.repository;

import com.jwt.AegisAuth.entity.BlacklistedTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlacklistedTokenRepository extends MongoRepository<BlacklistedTokenEntity, String> {
    boolean existsByToken(String token);
}
