package com.example.demo.config.security.filter;

import com.example.demo.config.security.JwtProps;
import com.example.demo.config.security.payload.UserPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

public class MyLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final SecretKey authKey;
    private final SecretKey refreshKey;

    public MyLoginFilter(AuthenticationManager authenticationManager, SecretKey authKey, SecretKey refreshKey) {
        this.authenticationManager = authenticationManager;
        this.authKey = authKey;
        this.refreshKey = refreshKey;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String contentType = request.getContentType();
            if (contentType.equalsIgnoreCase(JwtProps.CONTENT_TYPE)) {
                Authentication authentication = getAuthentication(request.getInputStream());
                return authenticationManager.authenticate(authentication);
            } else {
                throw new InvalidContentTypeException();
            }
        } catch (IOException | InvalidContentTypeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String authToken = buildAuthToken(authResult);
        addHeader(response, authToken);
        
        String refreshToken = buildRefreshToken(authResult);
        addCookie(response, refreshToken);
    }

    /* My Function */
    private void addHeader(HttpServletResponse response, String authToken) {
        response.addHeader(JwtProps.HEADER_TYPE, authToken);
    }

    private void addCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(JwtProps.REFRESH_KEY, refreshToken);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        response.addCookie(cookie);
    }
    
    Authentication getAuthentication(InputStream requestInputStream) throws IOException {
        UserPayload userPayload = new ObjectMapper()
                .readValue(requestInputStream, UserPayload.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPayload.getUsername(), userPayload.getPassword());
        return authentication;
    }

    private String buildAuthToken(Authentication authResult) {
        Header header = Jwts.header().setType(JwtProps.TOKEN_TYPE);
        LocalDateTime nowDateTime = LocalDateTime.now();


        return Jwts.builder()
                // HEADER
                .setHeader((Map<String, Object>) header)
                // PAYLOAD
                .setSubject(authResult.getName())
                .claim(JwtProps.CLAIM_KEY, authResult.getAuthorities())
                .setIssuedAt(Date.from(nowDateTime.atZone(ZoneOffset.systemDefault()).toInstant()))
                .setExpiration(Timestamp.valueOf(nowDateTime.plusMinutes(JwtProps.AUTH_EXPIRATION_MINUTE)))
                // SIGNATURE
                .signWith(authKey) // base64 format for Header, Payload and Secretkey
                .compact();
    }

    private String buildRefreshToken(Authentication authResult) {
        Header header = Jwts.header().setType(JwtProps.TOKEN_TYPE);
        LocalDateTime nowDateTime = LocalDateTime.now();

        return Jwts.builder()
                // HEADER
                .setHeader((Map<String, Object>) header) // alg, typ
                // PAYLOAD
                .setSubject(authResult.getName())   // sub
                .setIssuedAt(Date.from(nowDateTime.atZone(ZoneOffset.systemDefault()).toInstant())) // iat
                .setExpiration(Timestamp.valueOf(nowDateTime.plusMinutes(JwtProps.REFRESH_EXPIRATION_MINUTE)))   // exp
                // SIGNATURE
                .signWith(refreshKey) // base64 format for Header, Payload and Secretkey
                .compact();
    }
}