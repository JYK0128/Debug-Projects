package com.example.demo.config.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MyBlacklistService {
    ConcurrentHashMap<String, LocalDateTime> cache;

    public void logout(Jws<Claims> claimsJws){
        String jti = claimsJws.getBody().getId();
        cache.put(jti, LocalDateTime.now());
    }

    public boolean isLogout(Jws<Claims> claimsJws){
        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        String jti = claimsJws.getBody().getId();
        LocalDateTime preExp = cache.get(jti);

        return currentLocalDateTime.isBefore(preExp);
    }
}
