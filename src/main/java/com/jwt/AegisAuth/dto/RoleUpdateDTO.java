package com.jwt.AegisAuth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdateDTO {

    @NotBlank
    @Pattern(
            regexp = "USER|ADMIN",
            message = "Role must be USER or ADMIN"
    )
    private String role;
}
