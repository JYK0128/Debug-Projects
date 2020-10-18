package com.example.demo.config.oauth.google;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public class GoogleClientRegistration {
    public static ClientRegistration client() {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId("google");
        builder
                .clientName("Google")
                .clientId("278564592334-j2vlvrr7lc06tfglindqf564tg75h0qt.apps.googleusercontent.com")
                .clientSecret("UF_Crv3IRVPtZLm7DdVEggzs")
                .scope(new String[]{"openid", "profile", "email"})
                .redirectUriTemplate("{baseUrl}/oauth2/code/{registrationId}")
                .clientAuthenticationMethod(ClientAuthenticationMethod.POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userInfoAuthenticationMethod(AuthenticationMethod.HEADER)

                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub");
        return builder.build();
    }
}
