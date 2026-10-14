package com.storage.filestorageservice.auth.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.storage.filestorageservice.AbstractIntegrationTests;
import com.storage.filestorageservice.auth.dto.SignUpRequest;
import com.storage.filestorageservice.error.UsernameAlreadyExistsException;
import com.storage.filestorageservice.user.domain.User;
import com.storage.filestorageservice.user.domain.UserRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceIT extends AbstractIntegrationTests {

    @BeforeEach
    void clear() {
        userRepository.deleteAll();
    }

    @Autowired
    AuthService    authService;
    @Autowired
    UserRepository userRepository;

    @Test
    void signUp_shouldCreateUserInDataBase() throws UsernameAlreadyExistsException {
        authService.register(new SignUpRequest("user_1", "password"));

        User saved = userRepository.findByUsername("user_1").orElseThrow();
        assertThat(saved.getUsername()).isEqualTo("user_1");
        assertThat(saved.getPassword()).isNotEqualTo("password");
    }

    @Test
    void singUp_shouldThrowUsernameAlreadyExistsException() throws UsernameAlreadyExistsException {
        var saved = new User("user1", "hashed_pass");
        var user = userRepository.save(saved);
        SignUpRequest request = new SignUpRequest(user.getUsername(), "any_password");
        Assertions.assertThrows(UsernameAlreadyExistsException.class, () -> authService.register(request));
    }

}
