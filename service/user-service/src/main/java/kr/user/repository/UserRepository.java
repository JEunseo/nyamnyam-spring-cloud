package kr.user.repository;

import kr.user.document.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String username);

    // NaverUserId는 하나의 유저에게만 해당되므로 Mono로 수정
    Mono<User> findByNaverUserId(String naverUserId);
}