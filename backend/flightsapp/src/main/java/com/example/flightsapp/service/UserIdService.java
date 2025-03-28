package com.example.flightsapp.service;

import com.example.flightsapp.repository.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserIdService {
    private final SecretKey secretKey;

    public Long extractUserId(HttpServletRequest req) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(req.getHeader(HttpHeaders.AUTHORIZATION).substring(7))
                .getPayload().get("user_id", Long.class);
    }

    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claims(Map.of("user_id", user.getUserId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(secretKey)
                .compact();
    }
}
