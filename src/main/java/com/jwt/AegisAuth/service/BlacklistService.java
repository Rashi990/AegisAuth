package com.jwt.AegisAuth.service;

import com.jwt.AegisAuth.entity.BlacklistedTokenEntity;
import com.jwt.AegisAuth.repository.BlacklistedTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlacklistService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public BlacklistService(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    public void blacklistToken(String token, LocalDateTime expiryDate){
        BlacklistedTokenEntity blacklistedTokenEntity = BlacklistedTokenEntity.builder()
                .token(token)
                .expiryDate(expiryDate)
                .build();
        blacklistedTokenRepository.save(blacklistedTokenEntity);
    }
    public boolean isBlacklisted(String token){
        return blacklistedTokenRepository.existsByToken(token);
    }
}
