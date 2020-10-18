package com.example.demo.config.security;

import com.example.demo.config.security.filter.MyAuthFilter;
import com.example.demo.config.security.filter.MyLoginFilter;
import com.example.demo.config.security.filter.MyLogoutFilter;
import com.example.demo.config.security.filter.MyRefreshFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService myUserDetailsService;
    @Autowired @Qualifier("AuthKey")
    SecretKey authKey;
    @Autowired @Qualifier("RefreshKey")
    SecretKey refreshKey;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        int strength = 10;
        SecureRandom secureRandom = new SecureRandom();
        return new BCryptPasswordEncoder(strength, secureRandom);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password(encoder().encode("user")).roles("USER");
        auth.authenticationProvider(authProvider());
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
                .addFilterAfter(new MyLogoutFilter(), SecurityContextPersistenceFilter.class)
                .addFilterAfter(new MyLoginFilter(authenticationManager(), authKey, refreshKey), MyLogoutFilter.class)
                .addFilterAfter(new MyAuthFilter(authenticationManager(), authKey), MyLoginFilter.class)
                .addFilterAfter(new MyRefreshFilter(authenticationManager(), authKey, refreshKey), MyAuthFilter.class);
    }

    private void authorizationConfig(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login").permitAll()
                .anyRequest()
                .authenticated();
    }

    private void otherConfig(HttpSecurity http) throws Exception {
    }

}