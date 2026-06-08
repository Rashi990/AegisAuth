package com.jwt.AegisAuth.service;

import com.jwt.AegisAuth.dto.LoginRequestDTO;
import com.jwt.AegisAuth.dto.LoginResponseDTO;
import com.jwt.AegisAuth.dto.RegisterRequestDTO;
import com.jwt.AegisAuth.dto.RegisterResponseDTO;
import com.jwt.AegisAuth.entity.UserEntity;
import com.jwt.AegisAuth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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

    public UserEntity createUser(RegisterRequestDTO userData){
        UserEntity newUser = UserEntity.builder()
                .name(userData.getName())
                .email(userData.getEmail())
                .username(userData.getUsername())
                .password(passwordEncoder.encode(userData.getPassword()))
                .build();

        return userRepository.save(newUser);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){

        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword())
                    );
        }catch (BadCredentialsException e){
            return new LoginResponseDTO(null, null, "Invalid username or password", "error");
        }

        //Fetch real user from DB
        UserEntity user = userRepository
                .findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(()->new RuntimeException("User not found"));

        Map<String, Object> claims = new HashMap<String,Object>();
        claims.put("role","USER");
        claims.put("email",user.getEmail());
        claims.put("username", user.getUsername());

        String token = jwtService.getJWTToken(user.getUsername(), claims);

        //debug
        System.out.println("Role from token: " + jwtService.getFieldFromToken(token,"role"));

        return new LoginResponseDTO(token, LocalDateTime.now(), null, "SUCCESS");
    }

    public RegisterResponseDTO register(RegisterRequestDTO req){
        if (isUserEnable(req.getUsername())){
            return new RegisterResponseDTO(null, "User already exist in the system");
        }
        var userData = this.createUser(req);
        if (userData.getId()==null){
            return new RegisterResponseDTO(null, "System error");
        }
        return new RegisterResponseDTO(String.format("user registered at %s", userData.getId()),null);
    }

    private Boolean isUserEnable(String username){
        return userRepository.findByUsername(username).isPresent();
    }
}
