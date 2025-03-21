package com.saapsa.crudapp.repository;

import com.saapsa.crudapp.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("Sai Munikuntla");
        user.setEmail("sai.munikuntla@email-domain.com");
        user.setAge(21);

        userRepository.deleteAll()
                .then(userRepository.save(user))
                .block();
    }

    @Test
    public void testFindAll() {
        Flux<User> users = userRepository.findAll();

        StepVerifier.create(users)
                .expectNextMatches(user -> user.getName().equals("Sai Munikuntla"))
                .verifyComplete();
    }

    @Test
    public void testFindById() {
        Mono<User> userMono = userRepository.findById(user.getId());

        StepVerifier.create(userMono)
                .expectNextMatches(user -> user.getName().equals("Sai Munikuntla"))
                .verifyComplete();
    }

    @Test
    public void testFindByName() {
        Flux<User> users = userRepository.findByName("Sai Munikuntla");

        StepVerifier.create(users)
                .expectNextMatches(user -> user.getName().equals("Sai Munikuntla"))
                .verifyComplete();
    }

    @Test
    public void testDeleteById() {
        Mono<Void> deleteMono = userRepository.deleteById(user.getId());

        StepVerifier.create(deleteMono)
                .verifyComplete();

        Mono<User> userMono = userRepository.findById(user.getId());

        StepVerifier.create(userMono)
                .expectNextCount(0)
                .verifyComplete();
    }
}