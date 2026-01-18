package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Данные для регистрации нового пользователя")
public record RegisterDto(

        @Schema(
                description = "Email пользователя",
                example = "new.user@example.com"
        )
        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be valid")
        String email,

        @Schema(
                description = "Имя пользователя",
                example = "Ivan"
        )
        @NotBlank(message = "Firstname must not be blank")
        @Size(max = 100, message = "Firstname must be shorter than 100 characters")
        String firstname,

        @Schema(
                description = "Фамилия пользователя",
                example = "Petrov"
        )
        @NotBlank(message = "Lastname must not be blank")
        @Size(max = 100, message = "Lastname must be shorter than 100 characters")
        String lastname,

        @Schema(
                description = "Пароль пользователя",
                example = "StrongPassword123"
        )
        @NotBlank(message = "Password must not be blank")
        @Size(min = 6, max = 100, message = "Password length must be between 6 and 100")
        String password
) {
}

