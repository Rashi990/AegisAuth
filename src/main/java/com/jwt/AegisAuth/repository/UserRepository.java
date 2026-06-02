package com.jwt.AegisAuth.repository;

import com.jwt.AegisAuth.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity,String> {



}
