package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.net.URL;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String nickname;
    String email;
    String age_range;
    String birthday;
    String birthyear;
    String gender;
    String phone_number;
    String ci;
}
