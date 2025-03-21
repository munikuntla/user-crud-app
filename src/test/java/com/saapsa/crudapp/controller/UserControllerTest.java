package com.saapsa.crudapp.controller;

import com.saapsa.crudapp.domain.User;
import com.saapsa.crudapp.repository.UserRepository;
import com.saapsa.crudapp.api.model.UserRequest;
import com.saapsa.crudapp.service.UserService;

import io.r2dbc.spi.ConnectionFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@EnableR2dbcRepositories
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @MockBean
    private ConnectionFactory connectionFactory;

    @MockBean
    ConnectionFactoryInitializer initializer;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private UserRequest userRequest;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Sai Munikuntla");
        user.setEmail("sai.munikuntla@email-domain.com");
        user.setAge(21);

        userRequest = new UserRequest();
        userRequest.setName("Sai Munikuntla");
        userRequest.setEmail("sai.munikuntla@email-domain.com");
        userRequest.setAge(21);
    }

    @Test
    public void testCreateUser() {
        when(userService.createUser(any())).thenReturn(Mono.just(user));

        webTestClient.post().uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .isEqualTo(user);
    }

    @Test
    public void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(Flux.just(user));

        webTestClient.get().uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(1)
                .contains(user);
    }

    @Test
    public void testGetUserById() {
        when(userService.getUserById(anyLong())).thenReturn(Mono.just(user));

        webTestClient.get().uri("/api/v1/users/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .isEqualTo(user);
    }

    @Test
    public void testUpdateUser() {
        when(userService.updateUser(anyLong(), any())).thenReturn(Mono.just(user));

        webTestClient.put().uri("/api/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .isEqualTo(user);
    }

    @Test
    public void testDeleteUser() {
        when(userService.deleteUser(anyLong())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/v1/users/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();
    }
}