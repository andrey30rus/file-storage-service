package com.storage.filestorageservice.user.web;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.storage.filestorageservice.auth.security.CurrentUser;
import com.storage.filestorageservice.auth.security.SecurityUser;
import com.storage.filestorageservice.error.ErrorResponse;
import com.storage.filestorageservice.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Текущий пользователь")
@ApiResponse(responseCode = "500", description = "Неизвестная ошибка",
             content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
public class UserController {

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Текущий пользователь")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                 content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public UserResponse getCurrentUser(@CurrentUser SecurityUser user) {
        return new UserResponse(user.getUsername());
    }

}
