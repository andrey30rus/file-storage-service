package com.storage.filestorageservice.auth.dto;

public record SignInRequest(
        String username,
        String password
) {

}
