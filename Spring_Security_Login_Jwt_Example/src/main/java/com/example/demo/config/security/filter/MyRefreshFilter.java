package com.example.demo.config.security.filter;

import com.example.demo.config.security.JwtProps;
import com.example.demo.config.security.payload.UserPayload;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

public class MyRefreshFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final SecretKey authKey;
    private final SecretKey refreshKey;

    public MyRefreshFilter(AuthenticationManager authenticationManager, SecretKey authKey, SecretKey refreshKey) {
        this.authenticationManager = authenticationManager;
        this.authKey = authKey;
        this.refreshKey = refreshKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Header refreshHeader;
        Header authHeader;

        Claims refreshClaims;
        Claims authClaims;

        String authToken = getAuthToken(request);
        String refreshToken = getRefreshToken(request);

        authToken = removeTokenPrefix(authToken);
        refreshToken = removeTokenPrefix(refreshToken);

        try {
            Jws<Claims> validRefreshToken = isValidRefreshToken(refreshToken);
            refreshHeader = validRefreshToken.getHeader();
            refreshClaims = validRefreshToken.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            isValidAuthenticationToken(authToken);
        } catch (ExpiredJwtException e) {
            authHeader = e.getHeader();
            authClaims = e.getClaims();

            authToken = rearmAuthToken(authHeader, authClaims);
            refreshToken = rearmRefreshToken(refreshHeader, refreshClaims);

            Map<String, Object> payload = getPayload(authClaims);
            Authentication authentication = getAuthentication(payload);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            addHeader(response, authToken);
            addCookie(response, refreshToken);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }

    /* My function */
    private Authentication getAuthentication(Map<String, Object> data) {
        var username = (String) data.get(UserPayload.USERNAME_KEY);
        var authorities = (List<Map<String, String>>) data.get(JwtProps.CLAIM_KEY);

        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get(JwtProps.AUTHORITY_KEY)))
                .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
    }

    private Map<String, Object> getPayload(Claims claimsJws) {
        Map<String, Object> result = new HashMap<String, Object>();

        String username = claimsJws.getSubject();
        Object claim = claimsJws.get(JwtProps.CLAIM_KEY);

        result.put(UserPayload.USERNAME_KEY, username);
        result.put(JwtProps.CLAIM_KEY, claim);

        return result;
    }

    private String rearmAuthToken(Header authHeader, Claims authClaims) {
        LocalDateTime nowDateTime = LocalDateTime.now();

        return Jwts.builder()
                // HEADER
                .setHeader((Map<String, Object>) authHeader) // alg, typ
                // PAYLOAD
                .setClaims(authClaims)
                .setIssuedAt(Date.from(nowDateTime.atZone(ZoneOffset.systemDefault()).toInstant())) // iat
                .setExpiration(Timestamp.valueOf(nowDateTime.plusMinutes(JwtProps.AUTH_EXPIRATION_MINUTE)))   // exp
                // SIGNATURE
                .signWith(authKey) // base64 format for Header, Payload and Secretkey
                .compact();
    }

    private String rearmRefreshToken(Header refreshHeader, Claims refreshClaims) {
        LocalDateTime nowDateTime = LocalDateTime.now();

        return Jwts.builder()
                // HEADER
                .setHeader((Map<String, Object>) refreshHeader) // alg, typ
                // PAYLOAD
                .setClaims(refreshClaims)
                .setIssuedAt(Date.from(nowDateTime.atZone(ZoneOffset.systemDefault()).toInstant())) // iat
                .setExpiration(Timestamp.valueOf(nowDateTime.plusMinutes(JwtProps.REFRESH_EXPIRATION_MINUTE)))   // exp
                // SIGNATURE
                .signWith(refreshKey) // base64 format for Header, Payload and Secretkey
                .compact();
    }


    private String getAuthToken(HttpServletRequest request) {
        return request.getHeader(JwtProps.HEADER_TYPE);
    }

    private String getRefreshToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equalsIgnoreCase(JwtProps.REFRESH_KEY))
                .map(cookie -> cookie.getValue())
                .collect(Collectors.joining());
    }

    private Jws<Claims> isValidRefreshToken(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshKey).build()
                .parseClaimsJws(refreshToken);
    }

    private Jws<Claims> isValidAuthenticationToken(String authToken) {
        return Jwts.parserBuilder()
                .setSigningKey(authKey).build()
                .parseClaimsJws(authToken);
    }

    private String removeTokenPrefix(String token) {
        return token.replace(JwtProps.AUTH_TYPE, "");
    }

    private void addHeader(HttpServletResponse response, String authToken) {
        response.addHeader(JwtProps.HEADER_TYPE, authToken);
    }

    private void addCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(JwtProps.REFRESH_KEY, refreshToken);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
