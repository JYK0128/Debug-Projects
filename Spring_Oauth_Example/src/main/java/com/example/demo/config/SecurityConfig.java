package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    OauthService oauthService;
    @Autowired
    OidcService oidcService;

    @Bean
    public PasswordEncoder encoder() {
        int strength = 10;
        SecureRandom secureRandom = new SecureRandom();
        return new BCryptPasswordEncoder(strength, secureRandom);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        protectionConfig(http);
        customFilterConfig(http);
        authorizationConfig(http);
        otherConfig(http);
    }

    private void protectionConfig(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void customFilterConfig(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new ForwardedHeaderFilter(), OAuth2AuthorizationRequestRedirectFilter.class);
    }

    private void authorizationConfig(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll();
    }

    ObjectMapper objectMapper = new ObjectMapper();
    private void otherConfig(HttpSecurity http) throws Exception {
        http
//                .oauth2Client(withDefaults());
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauthService)
                                .oidcUserService(oidcService)
                        )
                        .redirectionEndpoint(redirect -> redirect
                                .baseUri("/login/oauth2/code/*")
                        )
                );
    }
}