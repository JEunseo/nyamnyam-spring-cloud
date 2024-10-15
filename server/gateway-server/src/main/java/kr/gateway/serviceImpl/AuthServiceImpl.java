package kr.gateway.serviceImpl;

import kr.gateway.document.LoginRequest;
import kr.gateway.document.OAuth2Request;
import kr.gateway.service.AuthService;
import kr.gateway.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<String> login(LoginRequest request) {
        return Mono.just(request.getUsername())
                .flatMap(username -> jwtTokenProvider.createToken(username));
    }

    @Override
    public Mono<String> oauthLogin(OAuth2Request request) {
        return Mono.just(request.getAccessToken())
                .flatMap(accessToken -> jwtTokenProvider.createToken(request.getProvider()));
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public String getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    @Override
    public Mono<String> refreshToken(String oldToken) {
        return jwtTokenProvider.refreshToken(oldToken);
    }

    @Override
    public Mono<Void> logout(String token) {
        return jwtTokenProvider.invalidateToken(token);
    }


}
