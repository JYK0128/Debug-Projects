package com.example.demo.config.security.filter;

import com.example.demo.config.security.JwtProps;
import com.example.demo.config.security.payload.UserPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MyAuthFilter extends BasicAuthenticationFilter {
    private final SecretKey secretKey;
    public MyAuthFilter(AuthenticationManager authenticationManager, SecretKey secretKey) {
        super(authenticationManager);
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = getToken(request);

        if (isToken(token)) {
            Jws<Claims> claims;

            String jwtToken = removeTokenPrefix(token);
            try {
                claims = isValidToken(jwtToken, secretKey);
            }catch (ExpiredJwtException e){
                chain.doFilter(request, response);
                return;
            }
            Map<String, Object> payload = getPayload(claims);
            Authentication authentication = getAuthentication(payload);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    /* My Function */
    private Authentication getAuthentication(Map<String, Object> data) {
        var username = (String) data.get(UserPayload.USERNAME_KEY);
        var authorities = (List<Map<String, String>>) data.get(JwtProps.CLAIM_KEY);

        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get(JwtProps.AUTHORITY_KEY)))
                .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
    }

    private Map<String, Object> getPayload(Jws<Claims> claimsJws){
        Map<String, Object> result = new HashMap<String, Object>();

        String username = claimsJws.getBody().getSubject();
        Object claim = claimsJws.getBody().get(JwtProps.CLAIM_KEY);

        result.put(UserPayload.USERNAME_KEY, username);
        result.put(JwtProps.CLAIM_KEY, claim);

        return result;
    }

    private Jws<Claims> isValidToken(String jwtToken, SecretKey secretKey){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey).build()
                .parseClaimsJws(jwtToken);
    }

    private String removeTokenPrefix(String token) {
        return token.replace(JwtProps.AUTH_TYPE, "");
    }

    private String getToken(HttpServletRequest request) {
        return request.getHeader(JwtProps.HEADER_TYPE);
    }

    private boolean isToken(String token) {
        return (token != null) && token.startsWith(JwtProps.AUTH_TYPE);
    }
}
