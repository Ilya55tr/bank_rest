package com.example.bankcards.controller.rest;

import com.example.bankcards.dto.card.CardCreateDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Cards",
        description = "Управление банковскими картами"
)
@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CardRestController {

    private final CardService cardService;
    private final SecurityUtil securityUtil;

    @Operation(
            summary = "Получить все карты (только ADMIN)",
            description = "Просмотр всех карт в системе с фильтрацией"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список карт"),
            @ApiResponse(responseCode = "403", description = "Нет прав ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public Page<CardDto> getAllCards(
            @RequestParam(required = false) CardStatus status,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate expirationBefore,

            Pageable pageable
    ) {
        return cardService.getAllCards(status, expirationBefore, pageable);
    }

    @Operation(
            summary = "Получить карту по ID (только ADMIN)"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{cardId}")
    public CardDto getCardById(
            @Parameter(description = "ID карты", example = "10")
            @PathVariable Long cardId
    ) {
        return cardService.getCardById(cardId);
    }


    @Operation(
            summary = "Создать карту (только ADMIN)",
            description = "Создание новой карты (только ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Карта создана"),
            @ApiResponse(responseCode = "403", description = "Нет прав ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CardDto> createCard(
            @Valid @RequestBody CardCreateDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cardService.createCard(dto));
    }

    @Operation(
            summary = "Получить свои карты (только USER)",
            description = "Список карт текущего пользователя с фильтрацией"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список карт"),
            @ApiResponse(responseCode = "403", description = "Нет прав USER")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<CardDto> getMyCards(
            @Parameter(description = "Статус карты")
            @RequestParam(required = false) CardStatus status,

            @Parameter(description = "Дата окончания до")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate expirationBefore,

            Pageable pageable
    ) {
        return cardService.getUserCards(
                securityUtil.getCurrentUserId(),
                status,
                expirationBefore,
                pageable
        );
    }

    @Operation(summary = "Получить свою карту по ID(только USER)")
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{cardId}")
    public CardDto getMyCard(
            @Parameter(description = "ID карты", example = "10")
            @PathVariable Long cardId
    ) {
        return cardService.getUserCard(
                securityUtil.getCurrentUserId(),
                cardId
        );
    }

    @Operation(summary = "Удалить карту(только ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "ID карты", example = "10")
            @PathVariable Long cardId
    ) {
        cardService.deleteCard(cardId);
    }
}

