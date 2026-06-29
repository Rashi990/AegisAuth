package com.jwt.AegisAuth.controller;

import com.jwt.AegisAuth.dto.*;
import com.jwt.AegisAuth.service.AuthService;
import com.jwt.AegisAuth.service.JWTService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO res = authService.login(loginRequestDTO);
        if (res.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO req) {
        RegisterResponseDTO res = authService.register(req);
        if (res.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        return ResponseEntity.ok(res);
    }

}


