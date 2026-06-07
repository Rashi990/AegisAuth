package com.jwt.AegisAuth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "user")
public class UserEntity {

    @Id
    private String id;
    private String name;
    private String email;
    @Indexed(unique = true)
    private String username;
    private String password;

}