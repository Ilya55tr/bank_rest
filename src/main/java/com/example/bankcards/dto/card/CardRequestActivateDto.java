package com.example.bankcards.dto.card;

import com.example.bankcards.entity.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Запрос на активацию карты")
public record CardRequestActivateDto(


        @Schema(
                description = "Запрашиваемый баланс (опционально)",
                example = "0",
                enumAsRef = true
        )
        @PositiveOrZero
        BigDecimal requestedBalance
) {
}
