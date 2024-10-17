package kr.gateway.config;

import kr.gateway.component.AuthHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class AuthRouter {

    private final AuthHandler authHandler;

    @Bean
    public RouterFunction<ServerResponse> authRoutes() {
        return RouterFunctions.route()
<<<<<<< HEAD
                .POST("/auth/login", authHandler::login)
=======
//                .POST("/auth/login", authHandler::login)
>>>>>>> 80cca43033e3ec9ef9c5917c91a2b4fe57bf4ee7
//                .POST("/auth/refresh", authHandler::refreshToken)
//                .POST("/auth/logout", authHandler::logout)
                .GET("/auth/oauth2/code/naver", authHandler::handleNaverCallback)
                .GET("/auth/oauth2/authorization/naver", authHandler::redirectToNaverLogin)
                .build();
    }
}
