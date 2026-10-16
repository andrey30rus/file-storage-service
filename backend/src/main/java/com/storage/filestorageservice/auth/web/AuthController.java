package com.storage.filestorageservice.auth.web;

import com.storage.filestorageservice.auth.dto.SignInRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.storage.filestorageservice.auth.dto.SignUpRequest;
import com.storage.filestorageservice.auth.service.AuthService;
import com.storage.filestorageservice.auth.service.SessionLogin;
import com.storage.filestorageservice.error.ErrorResponse;
import com.storage.filestorageservice.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Регистрация, вход и выход")
@ApiResponse(responseCode = "500", description = "Неизвестная ошибка",
             content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
public class AuthController {

    private final AuthService authenticationService;

    private final SessionLogin sessionLogin;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация", description = "Создаёт пользователя и сразу открывает сессию (cookie SESSION)")
    @ApiResponse(responseCode = "201", description = "Пользователь создан, сессия открыта")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                 content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Имя пользователя занято",
                 content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public UserResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        Authentication authentication = authenticationService.register(signUpRequest);
        sessionLogin.login(authentication, request, response);
        return new UserResponse(authentication.getName());
    }

    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Вход")
    @ApiResponse(responseCode = "200", description = "Вход выполнен, сессия открыта")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                 content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Неверные данные",
                 content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public UserResponse signIn(@RequestBody @Valid SignInRequest signInRequest,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        Authentication authentication = authenticationService.authenticate(signInRequest.username(), signInRequest.password());
        sessionLogin.login(authentication, request, response);
        return new UserResponse(authentication.getName());
    }

}
