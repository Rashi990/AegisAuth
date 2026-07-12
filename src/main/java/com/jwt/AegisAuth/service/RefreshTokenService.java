package com.jwt.AegisAuth.service;

import com.jwt.AegisAuth.entity.RefreshTokenEntity;
import com.jwt.AegisAuth.exception.BadRequestException;
import com.jwt.AegisAuth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshTokenEntity createRefreshToken(String username){

        refreshTokenRepository.deleteByUsername(username);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .username(username)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshTokenEntity getByToken(String token){
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));
    }

    public RefreshTokenEntity verifyExpiration(
            RefreshTokenEntity token
    ){
        if (token.getExpiryDate().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(token);
            throw new BadRequestException("Refresh token expired");
        }
        return token;
    }

}
