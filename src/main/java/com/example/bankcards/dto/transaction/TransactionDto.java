package com.example.bankcards.dto.transaction;

import com.example.bankcards.entity.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Информация о транзакции")
public record TransactionDto (

        @Schema(description = "ID транзакции")
        Long id,

        @Schema(description = "Карта отправителя")
        String fromCard,

        @Schema(description = "Карта получателя")
        String toCard,

        @Schema(description = "Сумма транзакции")
        BigDecimal amount,

        @Schema(
                description = "Статус транзакции",
                enumAsRef = true
        )
        TransactionStatus status,

        @Schema(
                description = "Дата и время создания транзакции"
        )
        LocalDateTime createdAt
) {}

