package com.example.bankcards.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Данные для перевода между картами")
public record TransactionCreateDto(

        @Schema(description = "Публичный номер карты отправителя")
        @NotNull
        UUID fromCardNumber,

        @Schema(description = "Публичный номер карты получателя")
        @NotNull
        UUID toCardNumber,

        @Schema(description = "Сумма перевода")
        @Positive
        BigDecimal amount
) {}



