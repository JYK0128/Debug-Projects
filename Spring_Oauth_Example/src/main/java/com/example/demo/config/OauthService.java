package com.example.demo.config;

import com.example.demo.domain.NaverUser;
import com.example.demo.repository.NaverRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class OauthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    NaverRepository naverRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        String userNameAttributeKey = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        NaverUser naverUser = objectMapper.convertValue(attributes, NaverUser.class);

        Optional<NaverUser> myUser = naverRepository.findByEmail(naverUser.getEmail());
        if(myUser.isEmpty()){
            naverRepository.save(naverUser);
        }

        return new DefaultOAuth2User(authorities, attributes, userNameAttributeKey);
    }
}
