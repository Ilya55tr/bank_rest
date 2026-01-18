package com.example.bankcards.controller.rest;

import com.example.bankcards.dto.card.CardRequestActivateDto;
import com.example.bankcards.dto.card.CardRequestBlockDto;
import com.example.bankcards.dto.card.CardRequestDto;
import com.example.bankcards.entity.RequestStatus;
import com.example.bankcards.entity.RequestType;
import com.example.bankcards.service.CardRequestService;
import com.example.bankcards.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Tag(
        name = "Card Requests",
        description = "Заявки пользователя на операции с картами"
)
@RestController
@RequestMapping("/api/card-requests")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CardRequestRestController {

    private final CardRequestService service;
    private final SecurityUtil securityUtil;

    @Operation(
            summary = "Создать заявку на активацию карты",
            description = "Пользователь отправляет запрос на активацию своей карты"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Заявка создана"),
            @ApiResponse(responseCode = "403", description = "Нет прав USER")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/activate")
    public ResponseEntity<CardRequestDto> createActivate(
            @Valid @RequestBody CardRequestActivateDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createActivateRequest(dto, securityUtil.getCurrentUserId()));
    }

    @Operation(
            summary = "Создать заявку на блокировку карты",
            description = "Пользователь отправляет запрос на блокировку своей карты"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Заявка создана"),
            @ApiResponse(responseCode = "403", description = "Нет прав USER")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/block")
    public ResponseEntity<CardRequestDto> createBlock(
            @Valid @RequestBody CardRequestBlockDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createBlockRequest(dto, securityUtil.getCurrentUserId()));
    }

    @Operation(
            summary = "Получить свои заявки",
            description = "Список всех заявок пользователя с пагинацией"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список заявок"),
            @ApiResponse(responseCode = "403", description = "Нет прав USER")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<CardRequestDto> getMyRequests(Pageable pageable) {
        return service.getUserRequests(
                securityUtil.getCurrentUserId(),
                pageable
        );
    }

    @Operation(
            summary = "Получить все заявки (ADMIN)",
            description = "Получение всех заявок с динамической фильтрацией и пагинацией"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список заявок"),
            @ApiResponse(responseCode = "403", description = "Нет прав ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public Page<CardRequestDto> getAllRequests(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long cardId,
            @RequestParam(required = false) RequestType type,
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) BigDecimal requestedBalance,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdTo,
            Pageable pageable
    ) {
        return service.getAllRequests(
                userId,
                cardId,
                type,
                status,
                requestedBalance,
                createdFrom,
                createdTo,
                pageable
        );
    }

    @Operation(summary = "Одобрить заявку (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{cardRequestId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approve(@PathVariable Long cardRequestId) {
        service.approve(cardRequestId);
    }

    @Operation(summary = "Отклонить заявку (ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{cardRequestId}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@PathVariable Long cardRequestId) {
        service.reject(cardRequestId);
    }

}