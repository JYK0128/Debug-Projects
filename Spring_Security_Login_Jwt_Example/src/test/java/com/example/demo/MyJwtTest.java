package com.example.demo;

import com.example.demo.config.security.JwtProps;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyJwtTest {
    SecretKey authKey;

    @BeforeAll
    void init() throws NoSuchAlgorithmException {
        JwtProps jwtProperty = new JwtProps();
        authKey = jwtProperty.AuthKey();
    }

    @Test
    void TokenNotValid(){
        Header header = Jwts.header().setType(JwtProps.TOKEN_TYPE);
        LocalDateTime nowDateTime = LocalDateTime.now();


        String token = Jwts.builder()
                // HEADER
                .setHeader((Map<String, Object>) header) // alg, typ
                // PAYLOAD
                .setSubject("user")   // sub
                .claim("test", "test value")  // {data}
                .setIssuedAt(Date.from(nowDateTime.atZone(ZoneOffset.systemDefault()).toInstant())) // iat
                .setExpiration(Timestamp.valueOf(nowDateTime.plusNanos(1)))   // exp
                // SIGNATURE
                .signWith(authKey) // base64 format for Header, Payload and Secretkey
                .compact();


        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("\\."); // Splitting header, payload and signature
        System.out.println("Headers: "+new String(decoder.decode(parts[0]))); // Header
        System.out.println("Payload: "+new String(decoder.decode(parts[1]))); // Payload


        try{
            Jwts.parserBuilder()
                    .setSigningKey(authKey).build()
                    .parseClaimsJws("dfd");
        }catch (ExpiredJwtException e){
            System.out.println("use exception");
            System.out.println(e.getClaims().get("test"));
        }
    }
}
