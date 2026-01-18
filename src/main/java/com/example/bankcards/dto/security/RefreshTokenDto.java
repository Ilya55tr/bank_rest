package com.example.bankcards.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import lombok.Data;

@Data
@Schema(description = "DTO для обновления access токена")
public class RefreshTokenDto {

    @Schema(
            description = "Refresh JWT токен",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoicmVmcmVzaCIsImV4cCI6MTcwMDAwMDAwMH0.signature"
    )
    private String refreshToken;
}

