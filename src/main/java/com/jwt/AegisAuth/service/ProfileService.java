package com.jwt.AegisAuth.service;

import com.jwt.AegisAuth.dto.ProfileDTO;
import com.jwt.AegisAuth.entity.ProfileEntity;
import com.jwt.AegisAuth.entity.UserEntity;
import com.jwt.AegisAuth.repository.ProfileRepository;
import com.jwt.AegisAuth.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public ProfileDTO getProfileByUserId(String userId){
        ProfileEntity profile = profileRepository.findByUserId(userId).orElse(null);

        if (profile==null){
            return null;
        }
        return new ProfileDTO(profile.getId(), profile.getImage(), profile.getStatus());
    }

    public ProfileDTO createProfile(String userId, ProfileDTO profileData){
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user==null){
            return null;
        }
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
}
