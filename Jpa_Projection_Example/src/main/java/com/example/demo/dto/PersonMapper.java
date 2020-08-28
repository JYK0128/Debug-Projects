package com.example.demo.dto;

import org.springframework.stereotype.Component;

@Component
public class PersonMapper {
    public String buildFullName(String firstName, String lastName) {
        return firstName + ' ' + lastName;
    }
}
