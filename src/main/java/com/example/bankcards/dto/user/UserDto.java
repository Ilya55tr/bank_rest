package com.example.bankcards.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Информация о пользователе")
public record UserDto(

        @Schema(description = "ID пользователя", example = "10")
        Long id,

        @Schema(description = "Email пользователя", example = "user@example.com")
        String email,

        @Schema(description = "Имя пользователя", example = "Ivan")
        String firstname,

        @Schema(description = "Фамилия пользователя", example = "Petrov")
        String lastname,

        @Schema(
                description = "Роль пользователя",
                example = "ROLE_USER",
                enumAsRef = true
        )
        String role
) {
}

