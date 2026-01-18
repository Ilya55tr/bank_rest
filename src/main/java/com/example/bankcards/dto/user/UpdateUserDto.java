package com.example.bankcards.dto.user;

import com.example.bankcards.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Данные для обновления пользователя (ADMIN)")
public record UpdateUserDto(

        @Schema(description = "Имя пользователя", example = "Ivan")
        @NotBlank
        String firstname,

        @Schema(description = "Фамилия пользователя", example = "Petrov")
        @NotBlank
        String lastname,

        @Schema(description = "Роль пользователя", example = "ADMIN")
        @NotNull
        Role role
) {
}
