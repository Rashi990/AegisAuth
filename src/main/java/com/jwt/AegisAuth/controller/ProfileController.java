package com.jwt.AegisAuth.controller;

import com.jwt.AegisAuth.dto.ProfileDTO;
import com.jwt.AegisAuth.entity.ProfileEntity;
import com.jwt.AegisAuth.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfileByUserId(@PathVariable String userId){
        ProfileDTO profile = profileService.getProfileByUserId(userId);
        if (profile==null){
            return ResponseEntity.
                    status(HttpStatus.NOT_FOUND).
                    body(Map.of("error","Profile not found"));
        }
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createProfile(
            @PathVariable String userId,
            @RequestBody ProfileDTO profileData){

       ProfileDTO profile = profileService.createProfile(userId, profileData);

       if (profile==null){
           return ResponseEntity.
                   status(HttpStatus.BAD_REQUEST).
                   body(Map.of("error","Invalid userId"));
       }
       return ResponseEntity.ok(profile);
    }
}
