package com.example.demo.config.security.payload;

import lombok.Getter;
import lombok.Setter;

import java.security.PublicKey;

@Getter @Setter
public class UserPayload {
    public static String USERNAME_KEY="username";

    String username;
    String password;
}
