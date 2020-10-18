package com.example.demo.config.oauth.kakao;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public class KakaoClientRegistration {
    public static ClientRegistration client() {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId("kakao");
        builder
                .clientName("Kakao")
                .clientId("6d70a01b8b65ab91b4ecfaea251c1422")
                .clientSecret("c8bNweg3AF2xMkX5xmhHaCikplsjVcqm")
                .scope(new String[]{"profile", "account_email"})
                .redirectUriTemplate("{baseUrl}/oauth2/code/{registrationId}")
                .clientAuthenticationMethod(ClientAuthenticationMethod.POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userInfoAuthenticationMethod(AuthenticationMethod.HEADER)

                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("kakao_account");
        return builder.build();
    }
}
