package com.example.ddukdoc.utils;

import com.example.ddukdoc.entity.Member;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        System.out.println("secretKey : " + secretKey.toString() + ", algrithm : " + secretKey.getAlgorithm());
    }

    //로그인 성공시 토큰 발행
    public String createJwt(Member member){
        Long expirationMs = 1000 * 60 * 3L;

        String jwt = Jwts.builder()
                           .claim("username", member.getUsername())
                           .claim("role", member.getRole())
                           .issuedAt(new Date(System.currentTimeMillis()))
                           .expiration(new Date(System.currentTimeMillis() + expirationMs))
                           .signWith(secretKey)
                           .compact();
        return jwt;
    }

    //토큰에서 사용자 정보 추출
    public String getUsername(String token){
        String username = Jwts.parser()
                              .verifyWith(secretKey)
                              .build()
                              .parseSignedClaims(token)
                              .getPayload()
                              .get("username", String.class);
        return username;
    }

    public String getRole(String token){
        String role = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
        return role;
    }
}
