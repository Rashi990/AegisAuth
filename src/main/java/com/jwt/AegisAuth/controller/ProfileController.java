package com.jwt.AegisAuth.controller;

import com.jwt.AegisAuth.dto.ProfileDTO;
import com.jwt.AegisAuth.service.ProfileService;
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

    @GetMapping("/me")
    public ResponseEntity<ProfileDTO> getMyProfile() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    @PostMapping("/create")
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileDTO profileData) {
        return ResponseEntity.ok(profileService.createProfile(profileData));
    }

    @PutMapping("/update")
    public ResponseEntity<ProfileDTO> updateProfile(@RequestBody ProfileDTO profileData) {
        return ResponseEntity.ok(profileService.updateProfile(profileData));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteProfile() {
        return ResponseEntity.ok(
                Map.of("message", profileService.deleteProfile())
        );
    }

}
