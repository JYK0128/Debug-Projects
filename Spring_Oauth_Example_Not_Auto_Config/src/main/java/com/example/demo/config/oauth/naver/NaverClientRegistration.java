package com.example.demo.config.oauth.naver;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public class NaverClientRegistration {

    public static ClientRegistration client() {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId("naver");
        builder
                .clientName("Naver")
                .clientId("KbUfgrZHHcyINOOffQlU")
                .clientSecret("678P53KC0p")
                .scope(new String[]{"name", "email", "openid"})
                .redirectUriTemplate("{baseUrl}/oauth2/code/{registrationId}")
                .clientAuthenticationMethod(ClientAuthenticationMethod.POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userInfoAuthenticationMethod(AuthenticationMethod.HEADER)

                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .userNameAttributeName("response");
        return builder.build();
    }
}