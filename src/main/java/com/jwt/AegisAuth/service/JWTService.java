package com.jwt.AegisAuth.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey secretKey;

    @PostConstruct
    public void init(){
        secretKey = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String getJWTToken(String username, Map<String,Object> claims){
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*15))
                .signWith(secretKey)
                .compact();
    }

    public String getUsername(String token){
        Claims data = getTokenData(token);
        if (data == null){
            return null;
        }
        return data.getSubject();
    }

    public Object getFieldFromToken(String token, String key){
        Claims data = getTokenData(token);
        if (data == null){
            return null;
        }
        return data.get(key);
    }

    private Claims getTokenData(String token){
        try {
            return Jwts
                    .parser()
                    .verifyWith(secretKey).build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (Exception e){
            return null;
        }
    }

    public LocalDateTime getExpiration(String token){
        Claims claims = getTokenData(token);
        return claims
                .getExpiration()
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
