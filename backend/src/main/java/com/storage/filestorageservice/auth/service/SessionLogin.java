package com.storage.filestorageservice.auth.service;

import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SessionLogin {

    private final SecurityContextRepository securityContextRepository;

}
