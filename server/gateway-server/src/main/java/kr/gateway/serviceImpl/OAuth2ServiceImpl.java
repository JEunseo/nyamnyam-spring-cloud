package kr.gateway.serviceImpl;//import kr.gateway.service.OAuth2Service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.core.OAuth2Token;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//@Service
//@RequiredArgsConstructor
//public class OAuth2ServiceImpl implements OAuth2Service {
//
//    private final WebClient webClient;
//
//    @Override
//    public Mono<OAuth2Token> getAccessToken(String code) {
//        String uri = "https://nid.naver.com/oauth2.0/token" +
//                "?client_id=YOUR_CLIENT_ID" +
//                "&client_secret=YOUR_CLIENT_SECRET" +
//                "&code=" + code +
//                "&grant_type=authorization_code";
//
//        return webClient.post()
//                .uri(uri)
//                .retrieve()
//                .bodyToMono(OAuth2Token.class);
//    }
//
//    @Override
//    public Mono<User> getUserInfo(String accessToken) {
//        return webClient.get()
//                .uri("https://openapi.naver.com/v1/nid/me")
//                .header("Authorization", "Bearer " + accessToken)
//                .retrieve()
//                .bodyToMono(User.class);
//    }
//}
