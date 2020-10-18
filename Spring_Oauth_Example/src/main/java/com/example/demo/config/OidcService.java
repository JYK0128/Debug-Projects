package com.example.demo.config;

import com.example.demo.domain.GoogleUser;
import com.example.demo.repository.GoogleRepository;
import com.example.demo.repository.KakaoRepository;
import com.example.demo.repository.NaverRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class OidcService extends OidcUserService {

    @Autowired
    GoogleRepository googleRepository;
    @Autowired
    NaverRepository naverRepository;
    @Autowired
    KakaoRepository kakaoRepository;

    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUserInfo userInfo = null;
        String clientName = userRequest.getClientRegistration().getClientName();
        OidcIdToken idToken = userRequest.getIdToken();

        if (clientName.equalsIgnoreCase("google")) {
            addGoogleUser(idToken);
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        DefaultOidcUser user = new DefaultOidcUser(authorities, userRequest.getIdToken(), userInfo);

        return user;
    }

    private void addGoogleUser(OidcIdToken idToken) {
        GoogleUser user = googleRepository.findByEmail(idToken.getEmail()).orElse(null);
        if (user == null) {
            try {
                user = objectMapper.readValue(objectMapper.writeValueAsString(idToken), GoogleUser.class);
                googleRepository.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}