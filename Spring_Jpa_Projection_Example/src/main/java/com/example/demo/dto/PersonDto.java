package com.example.demo.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JGlobalMap
public class PersonDto {
    private final String lastName;
}
