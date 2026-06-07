package com.jwt.AegisAuth.service;

import com.jwt.AegisAuth.dto.LoginRequestDTO;
import com.jwt.AegisAuth.dto.LoginResponseDTO;
import com.jwt.AegisAuth.entity.UserEntity;
import com.jwt.AegisAuth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    public UserEntity createUser(UserEntity userData){
        UserEntity newUser = UserEntity.builder()
                .name(userData.getName())
                .email(userData.getEmail())
                .username(userData.getUsername())
                .password(passwordEncoder.encode(userData.getPassword()))
                .build();

        return userRepository.save(newUser);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
//      Boolean userPresent = isUserEnable(loginRequestDTO.getUsername());
//      if (!userPresent) return new LoginResponseDTO(null,null,"User not found","error");

        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword())
                    );
        }catch (Exception e){
            return new LoginResponseDTO(
                    null,
                    null,
                    "User not found",
                    "error");
        }

        Map<String, Object> claims = new HashMap<String,Object>();
        claims.put("role","User");
        claims.put("email","company@gmail.com");

        String token = jwtService.getJWTToken(loginRequestDTO.getUsername(),claims);

        return new LoginResponseDTO(
              token,
              LocalDateTime.now(),
              null,
                "Login successful");
    }

    private Boolean isUserEnable(String username){
        return userRepository.findByUsername(username).isPresent();
    }
}
