package com.jwt.AegisAuth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileDTO {

    private String id;

    private String image;

    @Size(max = 100, message = "Status cannot exceed 100 characters")
    private Boolean status;
}
