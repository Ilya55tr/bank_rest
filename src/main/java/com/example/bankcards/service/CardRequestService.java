package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardRequestActivateDto;
import com.example.bankcards.dto.card.CardRequestBlockDto;
import com.example.bankcards.dto.card.CardRequestDto;
import com.example.bankcards.entity.RequestStatus;
import com.example.bankcards.entity.RequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CardRequestService {

    CardRequestDto createActivateRequest(
            CardRequestActivateDto dto,
            Long userId
    );

    CardRequestDto createBlockRequest(
            CardRequestBlockDto dto,
            Long userId
    );

    Page<CardRequestDto> getUserRequests(
            Long userId,
            Pageable pageable
    );

    Page<CardRequestDto> getAllRequests(
            Long userId,
            Long cardId,
            RequestType type,
            RequestStatus status,
            BigDecimal requestedBalance,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            Pageable pageable
    );

    void approve(Long cardRequestId);
    void reject(Long cardRequestId);


}
