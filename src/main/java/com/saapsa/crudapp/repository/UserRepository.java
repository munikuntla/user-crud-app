package com.saapsa.crudapp.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.saapsa.crudapp.domain.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    Flux<User> findAll();

    Mono<User> findById(@NonNull Long id);

    Mono<Void> deleteById(@NonNull Long id);
}
