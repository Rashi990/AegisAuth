package com.jwt.AegisAuth.service;

import com.jwt.AegisAuth.dto.ProfileDTO;
import com.jwt.AegisAuth.entity.ProfileEntity;
import com.jwt.AegisAuth.entity.UserEntity;
import com.jwt.AegisAuth.repository.ProfileRepository;
import com.jwt.AegisAuth.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    //Get logged in profile
    public ProfileDTO getMyProfile(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfileEntity profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return new ProfileDTO(
                profile.getId(),
                profile.getImage(),
                profile.getStatus());
    }

    //Get profile by userId
    public ProfileDTO getProfileByUserId(String userId){
        ProfileEntity profile = profileRepository.findByUserId(userId)
                .orElseThrow(()->new RuntimeException("Profile not found"));

        return new ProfileDTO(
                profile.getId(),
                profile.getImage(),
                profile.getStatus());
    }

    //Create profile
    public ProfileDTO createProfile(ProfileDTO profileData){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfileEntity profile = ProfileEntity.builder()
                .image(profileData.getImage())
                .status(profileData.getStatus())
                .user(user)
                .build();

        ProfileEntity savedProfile = profileRepository.save(profile);

        user.setProfile(savedProfile);
        userRepository.save(user);

        return new ProfileDTO(
                savedProfile.getId(),
                savedProfile.getImage(),
                savedProfile.getStatus());
    }

    //Update profile
    public ProfileDTO updateProfile(ProfileDTO profileData){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User not found"));

        ProfileEntity profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(()->new RuntimeException("Profile not found"));

        profile.setImage(profileData.getImage());
        profile.setStatus(profileData.getStatus());

        ProfileEntity updated = profileRepository.save(profile);

        return new ProfileDTO(
                updated.getId(),
                updated.getImage(),
                updated.getStatus()
        );
    }

    //Delete profile
    public String deleteProfile(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfileEntity profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profileRepository.delete(profile);

        return "Profile deleted successfully";

    }
}
