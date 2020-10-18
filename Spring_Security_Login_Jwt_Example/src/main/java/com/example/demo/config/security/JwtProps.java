package com.example.demo.config.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
public class JwtProps {
    private static final String AUTH_SECRET_TEXT = "authentication";
    private static final String REFRESH_SECRET_TEXT = "refresh";
    public static final String TOKEN_TYPE = "JWT";
    public static final String HEADER_TYPE = HttpHeaders.AUTHORIZATION;
    public static final String CONTENT_TYPE = "application/json";
    public static final String AUTH_TYPE = "Bearer" + " ";
    public static final String CLAIM_KEY = "authorities";
    public static final String AUTHORITY_KEY = "authority";
    public static final String REFRESH_KEY = "refresh";

    public static final long AUTH_EXPIRATION_MINUTE = 1 * 1 * 1;
    public static final long REFRESH_EXPIRATION_MINUTE = 30 * 24 * 60;

    @Bean
    public SecretKey AuthKey() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashbytes = digest.digest(AUTH_SECRET_TEXT.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(hashbytes);
    }

    @Bean
    public SecretKey RefreshKey() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashbytes = digest.digest(REFRESH_SECRET_TEXT.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(hashbytes);
    }
}
