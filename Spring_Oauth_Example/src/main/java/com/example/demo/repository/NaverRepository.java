package com.example.demo.repository;

import com.example.demo.domain.GoogleUser;
import com.example.demo.domain.NaverUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "naver")
public interface NaverRepository extends JpaRepository<NaverUser, Long> {
    Optional<NaverUser> findByEmail(String email);
}