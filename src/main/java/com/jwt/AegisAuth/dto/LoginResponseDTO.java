package com.jwt.AegisAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponseDTO {

    private String token;
    private LocalDateTime time;
    private String error;
    private String message;
}
