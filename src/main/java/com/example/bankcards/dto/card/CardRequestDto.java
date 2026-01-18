package com.example.bankcards.dto.card;

import com.example.bankcards.entity.RequestStatus;
import com.example.bankcards.entity.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;


@Schema(description = "Заявка пользователя на операцию с картой")
public record CardRequestDto(

        @Schema(description = "ID заявки", example = "100")
        Long id,

        @Schema(description = "ID пользователя", example = "42")
        Long userId,

        @Schema(description = "ID карты", example = "10")
        Long cardId,

        @Schema(
                description = "Тип запроса",
                example = "BLOCK",
                enumAsRef = true
        )
        RequestType type,

        @Schema(
                description = "Запрашиваемый баланс",
                example = "0"
        )
        BigDecimal requestedBalance,

        @Schema(
                description = "Статус заявки",
                example = "PENDING",
                enumAsRef = true
        )
        RequestStatus status,

        @Schema(
                description = "Дата создания заявки",
                example = "2026-01-15T14:30:00"
        )
        LocalDateTime createdAt
) {
}

