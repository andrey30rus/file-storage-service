package com.storage.filestorageservice.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SessionLogin {

    private final SecurityContextRepository securityContextRepository;

    public void login(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
    }

}
