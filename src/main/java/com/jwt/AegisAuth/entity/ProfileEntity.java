package com.jwt.AegisAuth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "profile")
public class ProfileEntity {

    @Id
    private String id;
    private String image;
    private Boolean status;

    //One-to-one relationship
    @DBRef
    @JsonIgnore
    private UserEntity user;
}
