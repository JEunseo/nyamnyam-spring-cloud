package kr.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration naverClientRegistration = ClientRegistration.withRegistrationId("naver")
                .clientId("e2iaB9q3A_kk1k7hX6Qi")
                .clientSecret("Av6eAE_PsV")
                .scope("email", "profile")
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .userNameAttributeName("response")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // 추가된 부분
                .redirectUri("http://localhost:8000/login/oauth2/code/{registrationId}")
                .build();

        return new InMemoryReactiveClientRegistrationRepository(naverClientRegistration);
    }
}