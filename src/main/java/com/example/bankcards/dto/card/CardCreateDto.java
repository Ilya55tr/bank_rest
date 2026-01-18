package com.example.bankcards.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Данные для создания банковской карты")
public record CardCreateDto(

        @Schema(
                description = "ID владельца карты",
                example = "42"
        )
        @NotNull(message = "Owner id must not be null")
        Long ownerId,

        @Schema(
                description = "Начальный баланс карты",
                example = "1000.00"
        )
        @NotNull(message = "Initial balance must not be null")
        @Positive(message = "Initial balance must be positive")
        BigDecimal initialBalance
) {
}
