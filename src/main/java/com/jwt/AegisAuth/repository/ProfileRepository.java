package com.jwt.AegisAuth.repository;

import com.jwt.AegisAuth.entity.ProfileEntity;
import com.jwt.AegisAuth.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<ProfileEntity,String> {
    Optional<ProfileEntity> findByUserId(String userId);
}
