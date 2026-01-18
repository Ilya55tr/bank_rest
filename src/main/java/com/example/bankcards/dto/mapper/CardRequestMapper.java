package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.card.CardRequestActivateDto;
import com.example.bankcards.dto.card.CardRequestBlockDto;
import com.example.bankcards.dto.card.CardRequestDto;
import com.example.bankcards.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CardRequestMapper {


    public CardRequest toEntity(
            CardRequestActivateDto dto,
            User user,
            Card card
    ) {
        return CardRequest.builder()
                .user(user)
                .card(card)
                .type(RequestType.ACTIVATE)
                .requestedBalance(dto.requestedBalance())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }


    public CardRequest toEntity(
            CardRequestBlockDto dto,
            User user,
            Card card
    ) {
        return CardRequest.builder()
                .user(user)
                .card(card)
                .type(RequestType.BLOCK)
                .requestedBalance(card.getBalance())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }


    public CardRequestDto toDto(CardRequest entity) {
        return new CardRequestDto(
                entity.getId(),
                entity.getUser().getId(),
                entity.getCard() != null ? entity.getCard().getId() : null,
                entity.getType(),
                entity.getRequestedBalance(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
