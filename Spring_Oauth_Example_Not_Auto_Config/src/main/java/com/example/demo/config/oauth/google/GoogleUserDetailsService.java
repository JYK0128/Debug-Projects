package com.example.demo.config.oauth.google;

import com.example.demo.domain.GoogleUser;
import com.example.demo.repository.GoogleRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoogleUserDetailsService implements UserDetailsService {
    @Autowired
    GoogleRepository repository;

    public UserDetails loadUserByAuthentication(Authentication authentication) throws URISyntaxException {
        String token = (String) authentication.getPrincipal();
        GoogleUser user = getUserInfo(token);

        String email = user.getEmail();
        repository.findByEmail(email).orElseGet(() -> saveUserInfo(user));

        ClientRegistration client = GoogleClientRegistration.client();
        String[] scopes = client.getScopes().toArray(String[]::new);
        List<GrantedAuthority> grantedAuthorities = buildGrantedAuthorities(scopes);

        return User.builder()
                .username(email)
                .password("")
                .authorities(grantedAuthorities)
                .build();
    }

    private GoogleUser getUserInfo(String token) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        URI tokenInfoURI = new URI("https://www.googleapis.com/oauth2/v3/userinfo");
        ResponseEntity<Map> userResponse = restTemplate.exchange(tokenInfoURI,
                HttpMethod.GET, entity, Map.class);
        Map userInfo = userResponse.getBody();
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GoogleUser user = objectMapper.convertValue(userInfo, GoogleUser.class);

        return user;
    }

    private List<GrantedAuthority> buildGrantedAuthorities(String[] scope) {
        String[] authorities = Arrays.stream(scope)
                .map(s -> "SCOPE_" + s).collect(Collectors.toList()).toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(authorities);
    }

    private GoogleUser saveUserInfo(GoogleUser user){
        repository.saveAndFlush(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        throw new UnsupportedOperationException();
    }
}