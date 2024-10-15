package kr.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public Mono<String> createToken(String userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMilliseconds);

        // 비밀 키를 Key 객체로 변환
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String token = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key) // 변경된 부분
                .compact();

        return Mono.just(token);
    }

    public Mono<Boolean> validateToken(String token) {
        try {
            Jwts.parserBuilder() // parserBuilder 사용
                    .setSigningKey(secret) // 비밀 키 설정
                    .build() // JwtParser 생성
                    .parseClaimsJws(token); // 토큰 파싱
            return Mono.just(true);
        } catch (ExpiredJwtException e) {
            // 만료된 토큰 처리
            return Mono.just(false);
        } catch (SignatureException e) {
            // 서명이 잘못된 토큰 처리
            return Mono.just(false);
        } catch (MalformedJwtException e) {
            // 형식이 잘못된 토큰 처리
            return Mono.just(false);
        } catch (Exception e) {
            // 기타 예외 처리
            return Mono.just(false);
        }
    }


    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Mono<String> refreshToken(String oldToken) {
        String userId = getUserIdFromToken(oldToken);
        return createToken(userId);
    }

    public Mono<Void> invalidateToken(String token) {
        return Mono.empty();
    }
}
