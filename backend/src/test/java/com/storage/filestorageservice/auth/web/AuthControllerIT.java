package com.storage.filestorageservice.auth.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;
import com.storage.filestorageservice.AbstractIntegrationTests;
import com.storage.filestorageservice.auth.dto.SignInRequest;
import com.storage.filestorageservice.auth.dto.SignUpRequest;
import com.storage.filestorageservice.user.domain.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthControllerIT extends AbstractIntegrationTests {

    @LocalServerPort
    int port;
    @Autowired
    UserRepository userRepository;

    RestTestClient rest;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rest = RestTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void signIn_shouldReturnUsernameAndOpenSession() {
        rest.post().uri("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SignUpRequest("user_1", "password"))
                .exchange()
                .expectStatus().isCreated();

        EntityExchangeResult<String> signIn = rest.post().uri("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SignInRequest("user_1", "password"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();

        assertThat(signIn.getResponseBody()).isEqualTo("{\"username\":\"user_1\"}");

        String[] sessionCookie = signIn.getResponseHeaders().getFirst(HttpHeaders.SET_COOKIE)
                .split(";")[0].split("=", 2);
        rest.get().uri("/api/user/me")
                .cookie(sessionCookie[0], sessionCookie[1])
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.username").isEqualTo("user_1");
    }

    @Test
    void signIn_withWrongPassword_shouldReturnUnauthorized() {
        rest.post().uri("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SignUpRequest("user_1", "password"))
                .exchange()
                .expectStatus().isCreated();

        rest.post().uri("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SignInRequest("user_1", "wrong-password"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().jsonPath("$.message").isEqualTo("Неверное имя пользователя или пароль");
    }

    @Test
    void signIn_withUnknownUser_shouldReturnUnauthorized() {
        rest.post().uri("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SignInRequest("ghost_user", "password"))
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().jsonPath("$.message").isEqualTo("Неверное имя пользователя или пароль");
    }

}
