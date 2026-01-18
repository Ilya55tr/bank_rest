package com.example.bankcards.dto.card;

import com.example.bankcards.entity.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Информация о банковской карте")
public record CardDto(


        @Schema(description = "ID карты", example = "10")
        Long id,

        @Schema(description = "Публичный номер карты")
        UUID publicNumber,


        @Schema(description = "ID владельца", example = "1")
        Long ownerId,

        @Schema(
                description = "Номер карты",
                example = "**** **** **** 1234"
        )
        String cardNumber,

        @Schema(
                description = "Дата окончания действия карты",
                example = "2028-12-31"
        )
        LocalDate expirationDate,

        @Schema(
                description = "Статус карты",
                example = "ACTIVE",
                enumAsRef = true
        )
        CardStatus status,

        @Schema(
                description = "Текущий баланс карты",
                example = "750.50"
        )
        BigDecimal balance
) {
}
