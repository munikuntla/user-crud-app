package com.saapsa.crudapp.service;

import com.saapsa.crudapp.domain.User;
import com.saapsa.crudapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User(1L, "Test User", "test@example.com", 25);
    }

    @Test
    public void testGetAllUsers() {
        // Given
        User user1 = new User(1L, "User One", "user1@example.com", 25);
        User user2 = new User(2L, "User Two", "user2@example.com", 30);
        
        when(userRepository.findAll()).thenReturn(Flux.just(user1, user2));

        // When
        Flux<User> result = userService.getAllUsers();

        // Then
        StepVerifier.create(result)
                .expectNext(user1)
                .expectNext(user2)
                .verifyComplete();
    }

    @Test
    public void testGetUserById() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Mono.just(testUser));

        // When
        Mono<User> result = userService.getUserById(1L);

        // Then
        StepVerifier.create(result)
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    public void testGetUserById_NotFound() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Mono.empty());

        // When
        Mono<User> result = userService.getUserById(99L);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    public void testCreateUser() {
        // Given
        User newUser = new User(null, "New User", "new@example.com", 30);
        User savedUser = new User(1L, "New User", "new@example.com", 30);
        
        when(userRepository.save(newUser)).thenReturn(Mono.just(savedUser));

        // When
        Mono<User> result = userService.createUser(newUser);

        // Then
        StepVerifier.create(result)
                .expectNext(savedUser)
                .verifyComplete();
    }

    @Test
    public void testUpdateUser() {
        // Given
        User existingUser = new User(1L, "Original User", "original@example.com", 25);
        User updatedUser = new User(1L, "Updated User", "updated@example.com", 30);
        
        when(userRepository.findById(1L)).thenReturn(Mono.just(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(updatedUser));

        // When
        Mono<User> result = userService.updateUser(1L, new User(null, "Updated User", "updated@example.com", 30));

        // Then
        StepVerifier.create(result)
                .expectNext(updatedUser)
                .verifyComplete();
    }

    @Test
    public void testDeleteUser() {
        // Given
        when(userRepository.deleteById(1L)).thenReturn(Mono.empty());

        // When
        Mono<Void> result = userService.deleteUser(1L);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }
}