package com.jwt.AegisAuth.service;

import com.jwt.AegisAuth.dto.*;
import com.jwt.AegisAuth.entity.UserEntity;
import com.jwt.AegisAuth.exception.BadRequestException;
import com.jwt.AegisAuth.exception.ResourceNotFoundException;
import com.jwt.AegisAuth.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public UserDTO getUserById(String id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        return mapToDTO(user);
    }

    public UserEntity createUser(RegisterRequestDTO userData){
        UserEntity newUser = UserEntity.builder()
                .name(userData.getName())
                .email(userData.getEmail())
                .username(userData.getUsername())
                .password(passwordEncoder.encode(userData.getPassword()))
                .role("USER")
                .build();

        return userRepository.save(newUser);
    }

    public void deleteUser(String id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    //Login
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
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        Map<String, Object> claims = new HashMap<String,Object>();
        claims.put("role",user.getRole());
        claims.put("email",user.getEmail());
        claims.put("username", user.getUsername());

        String token = jwtService.getJWTToken(user.getUsername(), claims);

        //debug
        System.out.println("Role from token: " + jwtService.getFieldFromToken(token,"role"));

        return new LoginResponseDTO(token, LocalDateTime.now(), null, "SUCCESS");
    }

    //Register
    public RegisterResponseDTO register(RegisterRequestDTO req) {

        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        UserEntity userData = createUser(req);

        return new RegisterResponseDTO(
                "User registered successfully with id: " + userData.getId(),
                null
        );
    }

    private UserDTO mapToDTO(UserEntity user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername()
        );
    }

    public CurrentUserDTO getCurrentUser(){
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new ResourceNotFoundException("User not found"));

        return new CurrentUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    public UserDTO updateUserRole(
            String id,
            RoleUpdateDTO dto){

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setRole(dto.getRole());
        return mapToDTO(userRepository.save(user));
    }


}
