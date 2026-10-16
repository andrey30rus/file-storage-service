package com.storage.filestorageservice.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "Имя пользователя обязательно")
        String username,
        @NotBlank(message = "Пароль обязателен")
        String password
) {

}
