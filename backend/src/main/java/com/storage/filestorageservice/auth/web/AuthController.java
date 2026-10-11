package com.storage.filestorageservice.auth.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.storage.filestorageservice.error.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "API for working with Authentication")
@ApiResponse(responseCode = "500", description = "Неизвестная ошибка",
             content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
public class AuthController {

//    private final AuthService authenticationService;
//
//    @PostMapping("/sign-up")
//    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(summary = "User registration")
//    @ApiResponse(responseCode = "201", description = "Successfully registered")
//    @ApiResponse(responseCode = "400", description = "Validation errors")
//    @ApiResponse(responseCode = "409", description = "Username already exists")
//    public UserResponseDto signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
//
//        return authenticationService.singUp(signUpRequestDto);
//    }

}
