package com.jwt.AegisAuth.service;

import com.jwt.AegisAuth.entity.UserEntity;
import com.jwt.AegisAuth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    public UserEntity createUser(UserEntity userData){
        UserEntity newUser = new UserEntity(
                userData.getName(),
                userData.getEmail(),
                userData.getUsername(),
                passwordEncoder.encode(userData.getPassword()));

                return userRepository.save(newUser);
    }
}
