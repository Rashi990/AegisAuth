package com.jwt.AegisAuth.controller;

import com.jwt.AegisAuth.dto.LoginRequestDTO;
import com.jwt.AegisAuth.dto.LoginResponseDTO;
import com.jwt.AegisAuth.entity.UserEntity;
import com.jwt.AegisAuth.service.AuthService;
import com.jwt.AegisAuth.service.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO res = authService.login(loginRequestDTO);
        if (res.getError()!=null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        return ResponseEntity.ok(res);
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
