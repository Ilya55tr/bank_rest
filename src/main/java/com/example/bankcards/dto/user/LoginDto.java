package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Данные для аутентификации пользователя")
public record LoginDto(

        @Schema(
                description = "Email пользователя",
                example = "user@example.com"
        )
        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be valid")
        String email,

        @Schema(
                description = "Пароль пользователя",
                example = "strongPassword123"
        )
        @NotBlank(message = "Password must not be blank")
        @Size(min = 6, max = 50, message = "Password length must be between 6 and 50")
        String password
) {
}

