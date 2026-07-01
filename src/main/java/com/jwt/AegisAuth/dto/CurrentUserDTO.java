package com.jwt.AegisAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserDTO {

    private String id;
    private String username;
    private String email;
    private String role;
}
