package com.example.demo.domain;

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
public class NaverUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String email; // "openapi@naver.com"
    String nickname; // "OpenAPI"
    URL profile_image; // "https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif"
    String age; // "40-49"
    String gender; // "F"
    String name; // "오픈 API"
    String birthday; // "10-01"
}
