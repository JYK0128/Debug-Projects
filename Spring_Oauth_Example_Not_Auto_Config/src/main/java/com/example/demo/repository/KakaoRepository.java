package com.example.demo.repository;

import com.example.demo.domain.KakaoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "kakao")
public interface KakaoRepository extends JpaRepository<KakaoUser, Long> {
    Optional<KakaoUser> findByEmail(String email);
}