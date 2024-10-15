package kr.gateway.service;

import kr.gateway.document.LoginRequest;
import kr.gateway.document.OAuth2Request;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<String> login(LoginRequest request);
    Mono<String> oauthLogin(OAuth2Request request);
    Mono<Boolean> validateToken(String token);
    String getUserIdFromToken(String token);
    Mono<String> refreshToken(String oldToken);  // 토큰 갱신
    Mono<Void> logout(String token);  // 로그아웃 처리
}
