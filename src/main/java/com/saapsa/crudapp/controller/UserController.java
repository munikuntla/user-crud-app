package com.saapsa.crudapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.saapsa.crudapp.service.UserService;
import com.saapsa.crudapp.api.UsersApi;
import com.saapsa.crudapp.api.model.User;
import com.saapsa.crudapp.api.model.UserRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController implements UsersApi {
    private final UserService userService;
    
    @GetMapping("/users")
    public Mono<ResponseEntity<Flux<User>>> getAllUsers(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(userService.getAllUsers()
                .map(com.saapsa.crudapp.domain.User::toApiUser)));
    }
    
    @GetMapping("/users/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable Long id, ServerWebExchange exchange) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user.toApiUser()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/users")
    public Mono<ResponseEntity<User>> createUser(@Valid @RequestBody Mono<UserRequest> userRequest
            , ServerWebExchange exchange) {
        return userRequest
                .map(com.saapsa.crudapp.domain.User::fromUserRequest)
                .flatMap(userService::createUser)
                .map(user -> ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(user.toApiUser()));
    }
    
    @PutMapping("/users/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable Long id
            , @Valid @RequestBody Mono<UserRequest> userRequest
            , ServerWebExchange exchange) {
        return userRequest
                .map(com.saapsa.crudapp.domain.User::fromUserRequest)
                .flatMap(user -> userService.updateUser(id, user))
                .map(updatedUser -> ResponseEntity.ok(updatedUser.toApiUser()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id, ServerWebExchange exchange) {
        return userService.deleteUser(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }
}
