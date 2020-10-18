package com.example.demo.config.oauth;

import com.example.demo.config.oauth.google.GoogleAuthenticationProvider;
import com.example.demo.config.oauth.google.GoogleClientRegistration;
import com.example.demo.config.oauth.kakao.KakaoAuthenticationProvider;
import com.example.demo.config.oauth.kakao.KakaoClientRegistration;
import com.example.demo.config.oauth.naver.NaverAuthenticationProvider;
import com.example.demo.config.oauth.naver.NaverClientRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
public class OauthConfig {
    private static List<String> clients = Arrays.asList("google", "naver", "kakao");
    @Autowired
    GoogleAuthenticationProvider googleAuthenticationProvider;
    @Autowired
    NaverAuthenticationProvider naverAuthenticationProvider;
    @Autowired
    KakaoAuthenticationProvider kakaoAuthenticationProvider;

    //Client Service
    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(
                clientRegistrationRepository());
    }

    //Client Repository
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(c -> getRegistration(c))
                .filter(registration -> registration != null)
                .collect(Collectors.toList());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(String client) {
        if (client.equals("google")) {
            return GoogleClientRegistration.client();
        }else if(client.equals("naver")){
            return NaverClientRegistration.client();
        }else if(client.equals("kakao")){
            return KakaoClientRegistration.client();
        }else{
            return null;
        }
    }

    //Authentication Manager Resolver
    @Bean
    AuthenticationManagerResolver<HttpServletRequest> resolver() {
        return request -> {
            System.out.println("URI: " + request.getRequestURI());
            System.out.println("URL: " + request.getRequestURL());
            String token = request.getHeader("Authorization");
            String issuer = getIssuer(token.replaceFirst("Bearer ", ""));

            if(issuer.equals("google")){
                return googleAuthenticationProvider::authenticate;
            }else if(issuer.equals("naver")){
                return naverAuthenticationProvider::authenticate;
            }else if(issuer.equals("kakao")){
                return kakaoAuthenticationProvider::authenticate;
            }else{
                return null;
            }
        };
    }

    private String getIssuer(String token){
        if (token.startsWith("ya29")) return "google";
        else if(token.startsWith("AAAA")) return "naver";
        else if(Pattern.matches("^.{43}AAAF1L.{5}$", token)) return "kakao";
        else return null;
    }
}