package com.jwt.AegisAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterRequestDTO {

    private String name;
    private String email;
    private String username;
    private String password;
}
