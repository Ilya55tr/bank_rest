package com.example.bankcards.dto.card;

import com.example.bankcards.entity.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Запрос на блокировку карты")
public record CardRequestBlockDto(

        @Schema(
                description = "ID карты",
                example = "10"
        )
        @Positive
        Long cardId
) {
}
