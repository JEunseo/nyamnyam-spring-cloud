package kr.gateway.component;

import kr.gateway.component.JwtTokenProvider;
import kr.gateway.document.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import org.springframework.core.ParameterizedTypeReference;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final WebClient webClient = WebClient.create();

//    // 1. 로그인 처리
//    public Mono<ServerResponse> login(ServerRequest request) {
//        return request.bodyToMono(LoginRequest.class)
//                .flatMap(req -> jwtTokenProvider.generateToken()
//                .flatMap(jwt -> ServerResponse.ok().bodyValue("Login successful. JWT: " + jwt))
//                .onErrorResume(e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(e.getMessage()));
//    }
//
//    // 2. 토큰 갱신 처리
//    public Mono<ServerResponse> refreshToken(ServerRequest request) {
//        return request.bodyToMono(String.class)
//                .flatMap(jwtTokenProvider::refreshToken)
//                .flatMap(newToken -> ServerResponse.ok().bodyValue("New Token: " + newToken))
//                .onErrorResume(e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue(e.getMessage()));
//    }
//
//    // 3. 로그아웃 처리
//    public Mono<ServerResponse> logout(ServerRequest request) {
//        return request.bodyToMono(String.class)
//                .flatMap(jwtTokenProvider::invalidateToken)
//                .then(ServerResponse.noContent().build())
//                .onErrorResume(e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
//    }

    // 4. 네이버 로그인 리디렉션 처리
    public Mono<ServerResponse> redirectToNaverLogin(ServerRequest request) {
        String state = UUID.randomUUID().toString();
        saveStateInSession(request.exchange(), state);

        String authorizationUri = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", "e2iaB9q3A_kk1k7hX6Qi")
                .queryParam("redirect_uri", "http://localhost:8000/auth/oauth2/code/naver")
                .queryParam("state", state)
                .queryParam("scope", "profile")
                .build()
                .toUriString();

        return ServerResponse.temporaryRedirect(URI.create(authorizationUri)).build();
    }

    // 세션에 state 값 저장
    private void saveStateInSession(ServerWebExchange exchange, String state) {
        exchange.getSession().doOnNext(session -> {
            session.getAttributes().put("oauth_state", state);
            System.out.println("Saved state in session: " + state);
        }).subscribe();
    }

    public Mono<ServerResponse> handleNaverCallback(ServerRequest request) {
        String code = request.queryParam("code").orElse("");
        String state = request.queryParam("state").orElse("");

        return exchangeCodeForToken(code, state)
                .flatMap(tokenResponse -> {
                    String accessToken = (String) tokenResponse.get("access_token");
                    System.out.println("Access Token: " + accessToken);

                    return requestUserInfo(tokenResponse);
                })
                .flatMap(userInfo -> {
                    System.out.println("User Info: " + userInfo);
                    return ServerResponse.ok().bodyValue(userInfo);
                })
                .onErrorResume(e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).bodyValue("Error: " + e.getMessage()));
    }



    // 네이버로 Access Token 요청
    private Mono<Map<String, Object>> exchangeCodeForToken(String code, String state) {
        String tokenUri = "https://nid.naver.com/oauth2.0/token";

        return webClient.post()
                .uri(tokenUri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=authorization_code&client_id=e2iaB9q3A_kk1k7hX6Qi" +
                        "&client_secret=Av6eAE_PsV&code=" + code + "&state=" + state +
                        "&redirect_uri=http://localhost:8000/auth/oauth2/code/naver")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});  // 제네릭 타입 명시
    }


    // 네이버에서 사용자 정보 요청
    private Mono<Map<String, Object>> requestUserInfo(Map<String, Object> tokenResponse) {
        String accessToken = (String) tokenResponse.get("access_token");

        return webClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    if (response.containsKey("response")) {
                        return Mono.just((Map<String, Object>) response.get("response"));
                    }
                    return Mono.error(new RuntimeException("Invalid user info response"));
                });
    }


}
