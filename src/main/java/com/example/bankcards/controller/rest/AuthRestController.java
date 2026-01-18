package com.example.bankcards.controller.rest;

import com.example.bankcards.dto.security.JwtAuthenticationDto;
import com.example.bankcards.dto.security.RefreshTokenDto;
import com.example.bankcards.dto.user.LoginDto;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;


@Tag(name = "Auth", description = "Аутентификация и JWT")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserService userService;

    @Operation(summary = "Логин пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "JWT токены"),
            @ApiResponse(responseCode = "403", description = "Неверные данные")
    })
    @PostMapping("/login")
    public JwtAuthenticationDto signIn(
            @Valid @RequestBody LoginDto loginDto
    ) throws AuthenticationException {
        return userService.singIn(loginDto);
    }

    @Operation(summary = "Обновление access token")
    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(
            @Valid @RequestBody RefreshTokenDto refreshTokenDto
    ) throws Exception {
        return userService.refreshToken(refreshTokenDto);
    }
}
