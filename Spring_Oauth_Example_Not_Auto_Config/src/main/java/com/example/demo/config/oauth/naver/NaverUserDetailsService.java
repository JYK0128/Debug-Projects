package com.example.demo.config.oauth.naver;

import com.example.demo.domain.NaverUser;
import com.example.demo.repository.NaverRepository;
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
public class NaverUserDetailsService implements UserDetailsService {
    @Autowired
    NaverRepository repository;

    public UserDetails loadUserByAuthentication(Authentication authentication) throws URISyntaxException {
        String token = (String) authentication.getPrincipal();
        NaverUser user = getUserInfo(token);

        String email = user.getEmail();
        repository.findByEmail(email).orElseGet(() -> saveUserInfo(user));

        ClientRegistration client = NaverClientRegistration.client();
        String[] scopes = client.getScopes().toArray(String[]::new);
        List<GrantedAuthority> grantedAuthorities = buildGrantedAuthorities(scopes);

        return User.builder()
                .username(email)
                .password("")
                .authorities(grantedAuthorities)
                .build();
    }

    private NaverUser getUserInfo(String token) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        URI tokenInfoURI = new URI("https://openapi.naver.com/v1/nid/me");
        ResponseEntity<Map> userResponse = restTemplate.exchange(tokenInfoURI,
                HttpMethod.GET, entity, Map.class);
        Map userInfo = (Map) userResponse.getBody().get("response");
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        NaverUser user = objectMapper.convertValue(userInfo, NaverUser.class);

        return user;
    }


    private List<GrantedAuthority> buildGrantedAuthorities(String[] scope) {
        String[] authorities = Arrays.stream(scope)
                .map(s -> "SCOPE_" + s).collect(Collectors.toList()).toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(authorities);
    }

    private NaverUser saveUserInfo(NaverUser user){
        repository.saveAndFlush(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        throw new UnsupportedOperationException();
    }
}