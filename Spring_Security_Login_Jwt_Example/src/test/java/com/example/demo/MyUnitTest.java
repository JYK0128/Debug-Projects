package com.example.demo;

import com.example.demo.config.security.JwtProps;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyUnitTest {
    ConcurrentHashMap<String, LocalDateTime> cache;

    void logout(Jws<Claims> claimsJws){
        String tokenId = claimsJws.getBody().getId();
        cache.put(tokenId, LocalDateTime.now());
    }

    String login(){
        String tokenID = "testID";
        Header header = Jwts.header().setType(JwtProps.TOKEN_TYPE);
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime expireDate = nowDateTime.plusMinutes(JwtProps.REFRESH_EXPIRATION_MINUTE);

        String token = Jwts.builder()
                // HEADER
                .setHeader((Map<String, Object>) header) // alg, typ
                // PAYLOAD
                .setSubject("test")   // sub
                .setIssuedAt(Date.from(nowDateTime.atZone(ZoneOffset.systemDefault()).toInstant())) // iat
                .setExpiration(Timestamp.valueOf(expireDate))   // exp
                .setId(tokenID)
                // SIGNATURE
                .signWith(Keys.hmacShaKeyFor("testSecretText".getBytes())) // base64 format for Header, Payload and Secretkey
                .compact();

        cache.put(tokenID, expireDate);

        return token;
    }

    @Test
    void Test1(){
       String token = login();

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("testSecretText".getBytes()))
                .build()
                .parseClaimsJws(token);
    }
}
