package com.storage.filestorageservice.auth.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.storage.filestorageservice.auth.dto.SignUpRequest;
import com.storage.filestorageservice.error.UsernameAlreadyExistsException;
import com.storage.filestorageservice.user.domain.User;
import com.storage.filestorageservice.user.domain.UserRepository;
import com.storage.filestorageservice.user.dto.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(SignUpRequest request) throws UsernameAlreadyExistsException {
        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        User userToSave = new User(request.username(), passwordEncoder.encode(request.password()));
        User savedUser;
        try {
            savedUser = userRepository.save(userToSave);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException(
                    "User with username: "
                            + userToSave.getUsername() + "is already exists");
        }
        return new UserResponse(savedUser.getUsername());

    }

}
