package com.example.bankcards.controller.rest;

import com.example.bankcards.dto.transaction.TransactionCreateDto;
import com.example.bankcards.dto.transaction.TransactionDto;
import com.example.bankcards.entity.TransactionStatus;
import com.example.bankcards.service.TransactionService;
import com.example.bankcards.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Transactions",
        description = "Операции пользователя"
)
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TransactionRestController {

    private final TransactionService transactionService;
    private final SecurityUtil securityUtil;

    @Operation(
            summary = "Просмотр всех транзакций (ADMIN)",
            description = """
                Позволяет администратору получить список всех транзакций в системе.
                Поддерживает фильтрацию по статусу и диапазону дат,
                а также постраничный вывод результатов.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Список транзакций успешно получен"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещён. Требуется роль ADMIN"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public Page<TransactionDto> getAllTransactions(
            @Parameter(
                    description = "Статус транзакции (необязательный фильтр)",
                    example = "SUCCESS"
            )
            @RequestParam(required = false)
            TransactionStatus status,

            @Parameter(
                    description = "Начальная дата и время (ISO-8601)",
                    example = "2026-01-01T00:00:00"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @Parameter(
                    description = "Конечная дата и время (ISO-8601)",
                    example = "2026-01-31T23:59:59"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            @ParameterObject
            Pageable pageable
    ) {
        return transactionService.getAllTransactions(status, from, to, pageable);
    }


    @Operation(
            summary = "Перевод средств",
            description = "Перевод средств между картами пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Транзакция выполнена"),
            @ApiResponse(responseCode = "403", description = "Нет прав USER")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionDto> transfer(
            @Valid @RequestBody TransactionCreateDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.transfer(securityUtil.getCurrentUserId(), dto));
    }

    @Operation(
            summary = "Получить свои транзакции",
            description = "История транзакций с фильтрацией и пагинацией"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список транзакций"),
            @ApiResponse(responseCode = "403", description = "Нет прав USER")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<TransactionDto> getMyTransactions(
            @Parameter(description = "Статус транзакции")
            @RequestParam(required = false) TransactionStatus status,

            @Parameter(description = "Дата от")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @Parameter(description = "Дата до")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            Pageable pageable
    ) {
        return transactionService.getUserTransactions(
                securityUtil.getCurrentUserId(),
                status,
                from,
                to,
                pageable
        );
    }
}
