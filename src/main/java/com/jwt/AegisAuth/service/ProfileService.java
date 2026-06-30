package com.jwt.AegisAuth.service;

import com.jwt.AegisAuth.dto.ChangePasswordDTO;
import com.jwt.AegisAuth.dto.ProfileDTO;
import com.jwt.AegisAuth.entity.ProfileEntity;
import com.jwt.AegisAuth.entity.UserEntity;
import com.jwt.AegisAuth.exception.BadRequestException;
import com.jwt.AegisAuth.exception.ResourceNotFoundException;
import com.jwt.AegisAuth.repository.ProfileRepository;
import com.jwt.AegisAuth.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Get logged in profile
    public ProfileDTO getMyProfile(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileEntity profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        return mapToDTO(profile);
    }

    //Get profile by userId
    public ProfileDTO getProfileByUserId(String userId){
        ProfileEntity profile = profileRepository.findByUserId(userId)
                .orElseThrow(()->new ResourceNotFoundException("Profile not found"));

        return mapToDTO(profile);
    }

    //Create profile
    public ProfileDTO createProfile(ProfileDTO profileData){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileEntity profile = ProfileEntity.builder()
                .image(profileData.getImage())
                .status(profileData.getStatus())
                .user(user)
                .build();

        ProfileEntity savedProfile = profileRepository.save(profile);

        user.setProfile(savedProfile);
        userRepository.save(user);

        return mapToDTO(savedProfile);
    }

    //Update profile
    public ProfileDTO updateProfile(ProfileDTO profileData){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        ProfileEntity profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(()->new ResourceNotFoundException("Profile not found"));

        profile.setImage(profileData.getImage());
        profile.setStatus(profileData.getStatus());

        ProfileEntity updated = profileRepository.save(profile);

        return mapToDTO(updated);
    }

    //Delete profile
    public String deleteProfile(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileEntity profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        profileRepository.delete(profile);

        return "Profile deleted successfully";
    }

    //Change password
    public void changePassword(ChangePasswordDTO dto){
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if(!passwordEncoder.matches(dto.getOldPassword(),
                user.getPassword())){

            throw new BadRequestException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    private ProfileDTO mapToDTO(ProfileEntity profile) {
        return new ProfileDTO(
                profile.getId(),
                profile.getImage(),
                profile.getStatus()
        );

    }
}
