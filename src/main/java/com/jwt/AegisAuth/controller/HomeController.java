package com.jwt.AegisAuth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("AegisAuth service is running");
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {

        Map<String, Object> res = new HashMap<>();
        res.put("status", "UP");
        res.put("service", "AegisAuth");

        return ResponseEntity.ok(res);
    }
}
