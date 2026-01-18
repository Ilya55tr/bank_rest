package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardCreateDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CardService {
    CardDto createCard(CardCreateDto dto);

    Page<CardDto> getUserCards(
            Long userId,
            CardStatus status,
            LocalDate expirationBefore,
            Pageable pageable
    );

    CardDto getUserCard(Long userId, Long cardId);

    Page<CardDto> getAllCards(
            CardStatus status,
            LocalDate expirationBefore,
            Pageable pageable
    );

    CardDto getCardById(Long cardId);

    void deleteCard(Long cardId);

}
