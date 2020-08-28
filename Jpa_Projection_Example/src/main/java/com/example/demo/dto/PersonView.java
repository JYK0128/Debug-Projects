package com.example.demo.dto;

import org.springframework.beans.factory.annotation.Value;

//@ProjectedPayload
public interface PersonView {
    String getFirstName();
    String getLastName();

//    @Value("#{target.firstName + ' ' + target.lastName}")
    @Value("#{@personMapper.buildFullName(target.firstName, target.lastName)}")
    String getFullName();
}
