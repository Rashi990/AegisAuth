package com.jwt.AegisAuth.controller;

import com.jwt.AegisAuth.entity.UserEntity;
import com.jwt.AegisAuth.service.AuthService;
import com.jwt.AegisAuth.service.JWTService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JWTService jwtService;
    private final AuthService authService;

    public AuthController(JWTService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(){
        return jwtService.getJWTToken();
    }

    @GetMapping("/username")
    public String getUsername(@RequestParam String token){
        return jwtService.getUsername(token);
    }

    @GetMapping("/users")
    public List<UserEntity> getAllUsers(){
        return authService.getAllUsers();
    }

    @PostMapping("/create-user")
    public UserEntity createUser(@RequestBody UserEntity user){
        return authService.createUser(user);
    }
}
