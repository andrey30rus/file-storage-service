package com.storage.filestorageservice.user;

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
import com.storage.filestorageservice.auth.dto.SignUpRequest;
import com.storage.filestorageservice.user.domain.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerIT extends AbstractIntegrationTests {

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
    void me_shouldReturnUsernameOfSignedUpUser() {
        EntityExchangeResult<String> signUp = rest.post().uri("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SignUpRequest("user_1", "password"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class)
                .returnResult();

        String[] sessionCookie = signUp.getResponseHeaders().getFirst(HttpHeaders.SET_COOKIE).split(";")[0].split("=", 2);
        assertThat(sessionCookie[0]).isEqualTo("SESSION");

        rest.get().uri("/api/user/me")
                .cookie(sessionCookie[0], sessionCookie[1])
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"username\":\"user_1\"}");
    }

    @Test
    void me_withoutSession_shouldReturnUnauthorized() {
        rest.get().uri("/api/user/me")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().jsonPath("$.message").isEqualTo("Пользователь не авторизован");
    }

}
