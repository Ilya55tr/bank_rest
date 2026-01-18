package com.example.bankcards.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "JWT токены после успешной аутентификации")
public class JwtAuthenticationDto {

    @Schema(
            description = "Access JWT токен",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjMiLCJleHAiOjE2OTk5OTk5OTl9.signature"
    )
    private String token;

    @Schema(
            description = "Refresh JWT токен",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoicmVmcmVzaCIsImV4cCI6MTcwMDAwMDAwMH0.signature"
    )
    private String refreshToken;
}
