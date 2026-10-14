package com.storage.filestorageservice.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "Имя пользователя обязательно")
        @Size(min = 5, max = 20, message = "Имя пользователя должно содержать от 5 до 20 символов")
        String username,
        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, message = "Пароль должен содержать не менее 8 символов") String password
) {

}
