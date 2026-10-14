package com.storage.filestorageservice.auth.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.storage.filestorageservice.auth.dto.SignUpRequest;
import com.storage.filestorageservice.auth.security.SecurityUser;
import com.storage.filestorageservice.error.UsernameAlreadyExistsException;
import com.storage.filestorageservice.user.domain.User;
import com.storage.filestorageservice.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public Authentication register(SignUpRequest request) throws UsernameAlreadyExistsException {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        User savedUser;
        try {
            savedUser = userRepository.saveAndFlush(new User(request.username(), passwordEncoder.encode(request.password())));
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException(
                    "Имя пользователя занято");
        }
        SecurityUser principal = new SecurityUser(savedUser);
        return UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities());
    }

    public Authentication authenticate(String username, String rawPassword) {
        return authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, rawPassword));
    }


}
