package com.saapsa.crudapp.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.saapsa.crudapp.domain.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.lang.NonNull;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Flux<User> findAll();

    Mono<User> findById(@NonNull Long id);

    @Query("SELECT * FROM users WHERE name = :name")
    Flux<User> findByName(@NonNull String name);

    Mono<Void> deleteById(@NonNull Long id);
}
