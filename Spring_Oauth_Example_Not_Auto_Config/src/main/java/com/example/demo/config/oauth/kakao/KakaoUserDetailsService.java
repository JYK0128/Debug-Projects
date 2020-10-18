package com.example.demo.config.oauth.kakao;

import com.example.demo.domain.KakaoUser;
import com.example.demo.repository.KakaoRepository;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class KakaoUserDetailsService implements UserDetailsService {
    @Autowired
    KakaoRepository repository;

    public UserDetails loadUserByAuthentication(Authentication authentication) throws URISyntaxException {
        String token = (String) authentication.getPrincipal();
        KakaoUser user = getUserInfo(token);

        String email = user.getEmail();
        repository.findByEmail(email).orElseGet(() -> saveUserInfo(user));

        ClientRegistration client = KakaoClientRegistration.client();
        String[] scopes = client.getScopes().toArray(String[]::new);
        List<GrantedAuthority> grantedAuthorities = buildGrantedAuthorities(scopes);
        grantedAuthorities.add(new SimpleGrantedAuthority("SCOPE_openid"));

        return User.builder()
                .username(email)
                .password("")
                .authorities(grantedAuthorities)
                .build();
    }

    private KakaoUser getUserInfo(String token) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        URI tokenInfoURI = new URI("https://kapi.kakao.com/v2/user/me");
        ResponseEntity<Map> userResponse = restTemplate.exchange(tokenInfoURI,
                HttpMethod.GET, entity, Map.class);
        Map userInfo = (Map) userResponse.getBody().get("kakao_account");

        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        KakaoUser user = objectMapper.convertValue(userInfo, KakaoUser.class);

        Map profile = (Map) userInfo.get("profile");
        user.setNickname((String) profile.get("nickname"));

        return user;
    }

    private List<GrantedAuthority> buildGrantedAuthorities(String[] scope) {
        String[] authorities = Arrays.stream(scope)
                .map(s -> "SCOPE_" + s).collect(Collectors.toList()).toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(authorities);
    }

    private KakaoUser saveUserInfo(KakaoUser user){
        repository.saveAndFlush(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        throw new UnsupportedOperationException();
    }
}