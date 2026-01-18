package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.entity.Card;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardDto toDto(Card card) {
        return new CardDto(
                card.getId(),
                card.getPublicNumber(),
                card.getOwner().getId(),
                maskCardNumber(card.getLast4()),
                card.getExpirationDate(),
                card.getStatus(),
                card.getBalance()
        );
    }

    private String maskCardNumber(String last4) {
        return "**** **** **** " + last4;
    }
}
